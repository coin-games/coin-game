<template>
  <div class="game-footer">
    <div class="time-bar">
      <div
        class="time-fill"
        :style="{ width: `${(timeLeft / maxTime) * 100}%` }"
      ></div>
    </div>
    <div class="time-text">남은 시간: {{ timeLeft }}초</div>
    <button class="reset-button" @click="$emit('reset')">처음으로</button>
  </div>
</template>

<script setup>
const props = defineProps({
  timeLeft: Number,
  maxTime: Number,
});
</script>

<style scoped>
.game-footer {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.time-bar {
  width: 300px;
  height: 18px;
  background-color: #dbeefb;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid #4fc3f7;
}

.time-fill {
  height: 100%;
  background-color: #4fc3f7;
  transition: width 0.3s ease;
}

.time-text {
  font-size: 1rem;
  font-weight: 500;
  color: #444;
}

.reset-button {
  padding: 8px 16px;
  font-size: 1rem;
  background-color: #81d4fa;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
  transition: background-color 0.2s ease;
}

.reset-button:hover {
  background-color: #4fc3f7;
}
</style>