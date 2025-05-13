<template>
  <div class="modal-backdrop">
    <div class="modal">
      <h2>⏰ 게임 종료!</h2>
      <p>시간이 종료되었습니다.</p>
      <input
        v-model="name"
        placeholder="이름을 입력하세요"
        class="name-input"
      />
      <div class="modal-buttons">
        <button @click="handleConfirm">확인</button>
        <button class="skip" @click="$emit('skip')">등록 안함</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: String
});
const emit = defineEmits(['update:modelValue', 'confirm', 'skip']);

const name = ref(props.modelValue || '');

watch(() => props.modelValue, (val) => {
  name.value = val;
});

watch(name, (val) => {
  emit('update:modelValue', val);
});

const handleConfirm = () => {
  if (!name.value.trim()) {
    alert('이름을 입력해주세요!');
    return;
  }
  emit('confirm', name.value);
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal {
  background: white;
  padding: 30px 40px;
  border-radius: 16px;
  border: 2px solid #4fc3f7;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  text-align: center;
}

.modal h2 {
  margin-bottom: 12px;
  font-size: 1.6rem;
  color: #333;
}

.modal p {
  margin-bottom: 16px;
  color: #666;
}

.modal input {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  box-sizing: border-box;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 16px;
}

.modal-buttons button {
  padding: 8px 20px;
  font-size: 1rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.modal-buttons button:first-child {
  background-color: #4fc3f7;
  color: white;
}

.modal-buttons button:first-child:hover {
  background-color: #039be5;
}

.modal-buttons .skip {
  background-color: #e0e0e0;
  color: #444;
}

.modal-buttons .skip:hover {
  background-color: #bdbdbd;
}
</style>