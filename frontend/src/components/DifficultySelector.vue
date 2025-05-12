<template>
  <div class="difficulty-select">
    <h2>난이도를 선택하세요</h2>
    <button
      v-for="(info, level) in difficulties"
      :key="level"
      @click="$emit('select', level)"
    >
      {{ level }} ({{ info.cols }} × {{ info.rows }})
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  difficulties: Object
});
const emit = defineEmits(['select']);
</script>

<style scoped>
.difficulty-select {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 100px;
  gap: 20px;
}

.difficulty-select button {
  font-size: 1.2rem;
  padding: 10px 20px;
  cursor: pointer;
}
</style>