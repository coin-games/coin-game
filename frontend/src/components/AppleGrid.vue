<script setup>
import { ref, watch, onMounted } from 'vue';
import Apple from '@/components/Apple.vue';

const props = defineProps({
  cols: Number,
  rows: Number,
  initialApples: Array,
  gameOver: Boolean
});

const emit = defineEmits(['update:score', 'update:time', 'game-end']);

const apples = ref([]);
const appleRefs = ref([]);
const selecting = ref(false);
const dragBoxStyle = ref({ display: 'none' });
const start = ref({ x: 0, y: 0 });
const end = ref({ x: 0, y: 0 });

watch(() => props.initialApples, (newVal) => {
  apples.value = [...newVal];
  appleRefs.value = Array(newVal.length).fill(null);
}, { immediate: true });

const setAppleRef = (el, i) => {
  if (el) appleRefs.value[i] = el;
};

const onMouseDown = (e) => {
  if (props.gameOver) return;

  start.value = { x: e.clientX, y: e.clientY };
  end.value = { x: e.clientX, y: e.clientY };
  selecting.value = true;

  apples.value.forEach((apple) => (apple.selected = false));
  dragBoxStyle.value = {
    display: 'block',
    left: `${start.value.x}px`,
    top: `${start.value.y}px`,
    width: '0px',
    height: '0px',
  };
};

const onMouseMove = (e) => {
  if (!selecting.value || props.gameOver) return;

  end.value = { x: e.clientX, y: e.clientY };
  const x = Math.min(start.value.x, end.value.x);
  const y = Math.min(start.value.y, end.value.y);
  const w = Math.abs(start.value.x - end.value.x);
  const h = Math.abs(start.value.y - end.value.y);

  dragBoxStyle.value = {
    display: 'block',
    left: `${x}px`,
    top: `${y}px`,
    width: `${w}px`,
    height: `${h}px`,
  };

  appleRefs.value.forEach((el, i) => {
    if (apples.value[i].hidden) return;
    const rect = el.getBoundingClientRect();
    const cx = rect.left + rect.width / 2;
    const cy = rect.top + rect.height / 2;
    apples.value[i].selected = (cx >= x && cx <= x + w && cy >= y && cy <= y + h);
  });
};

const onMouseUp = () => {
  if (!selecting.value) return;

  selecting.value = false;
  dragBoxStyle.value = { display: 'none' };

  let sum = 0;
  let bonus = 0;
  apples.value.forEach((apple) => {
    if (apple.selected && !apple.hidden) {
      sum += apple.number;
      bonus++;
    }
  });

  if (sum === 10) {
    emit('update:score', sum * bonus);
    emit('update:time', 1);

    apples.value.forEach((apple) => {
      if (apple.selected) apple.hidden = true;
    });
  }

  apples.value.forEach((apple) => {
    apple.selected = false;
  });
};

onMounted(() => {
  document.addEventListener('mouseup', onMouseUp);
  document.addEventListener('mousemove', onMouseMove);
});
</script>

<template>
  <div class="apple-grid-wrapper" @mousedown="onMouseDown">
    <div class="drag-box" :style="dragBoxStyle"></div>
    <div class="apple-grid" :style="{ gridTemplateColumns: `repeat(${cols}, auto)` }">
      <div
        v-for="(apple, i) in apples"
        :key="i"
        class="apple-wrapper"
        :ref="(el) => setAppleRef(el, i)"
      >
        <Apple
          :number="apple.number"
          :selected="apple.selected"
          :hidden="apple.hidden"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.apple-grid-wrapper {
  position: relative;
}
.apple-grid {
  display: grid;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
}
.apple-wrapper {
  width: 50px;
  height: 50px;
}
.drag-box {
  position: fixed;
  border: 2px dashed #4fc3f7;
  background-color: rgba(135, 206, 250, 0.15);
  z-index: 1000;
  pointer-events: none;
}
</style>