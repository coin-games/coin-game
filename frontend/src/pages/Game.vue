<script setup>
import { ref } from 'vue';
import AppleGrid from '@/components/AppleGrid.vue';
import GameFooter from '@/components/GameFooter.vue';
import DifficultySelector from '@/components/DifficultySelector.vue';
import GameOverModal from '@/components/GameOverModal.vue';
import ThemeSelector from '@/components/ThemeSelector.vue';

const difficulties = {
  쉬움: { cols: 9, rows: 5 },
  보통: { cols: 15, rows: 9 },
  어려움: { cols: 17, rows: 10 },
};

const apples = ref([]);
const cols = ref(0);
const rows = ref(0);
const gameStarted = ref(false);
const gameOver = ref(false);
const score = ref(0);
const timeLeft = ref(0);
const maxTime = ref(60);
const timer = ref(null);
const username = ref('');
const currentDifficulty = ref('');

const selectDifficulty = (level) => {
  currentDifficulty.value = level;
  cols.value = difficulties[level].cols;
  rows.value = difficulties[level].rows;

  const count = cols.value * rows.value;
  apples.value = Array.from({ length: count }, () => ({
    selected: false,
    hidden: false,
    number: Math.ceil(9 * Math.random()),
  }));

  score.value = 0;
  timeLeft.value = maxTime.value;
  gameStarted.value = true;
  gameOver.value = false;

  if (timer.value) clearInterval(timer.value);
  timer.value = setInterval(() => {
    timeLeft.value--;
    if (timeLeft.value <= 0) {
      clearInterval(timer.value);
      gameOver.value = true;
    }
  }, 1000);
};

const resetGame = () => {
  gameStarted.value = false;
  gameOver.value = false;
  apples.value = [];
  score.value = 0;
  timeLeft.value = 0;
  clearInterval(timer.value);
};

const handleGameOverConfirm = () => {
  if (!username.value.trim()) {
    alert('이름을 입력해주세요!');
    return;
  }

  alert(`${username.value}님의 점수: ${score.value}점이 기록되었습니다.`);
  resetGame();
};

const skipRegistration = () => {
  alert('기록하지 않고 돌아갑니다.');
  resetGame();
};
</script>

<template>
  <div class="game-container">
    <DifficultySelector
      v-if="!gameStarted"
      :difficulties="difficulties"
      @select="selectDifficulty"
    />

    <div v-else class="game-content">
      <!-- 상단 점수 -->
      <div class="score-row">
        <div class="score-box">점수: {{ score }}</div>
      </div>

      <!-- 사과 게임판 -->
      <AppleGrid
        :cols="cols"
        :rows="rows"
        :initialApples="apples"
        :gameOver="gameOver"
        @update:score="(val) => score += val"
        @update:time="(val) => timeLeft += val"
      />

      <!-- 하단 타이머/버튼 -->
      <GameFooter
        :timeLeft="timeLeft"
        :maxTime="maxTime"
        @reset="resetGame"
      />

      <!-- 게임 종료 모달 -->
      <GameOverModal
        v-if="gameOver"
        v-model="username"
        @confirm="handleGameOverConfirm"
        @skip="skipRegistration"
      />
    </div>

    <ThemeSelector @theme-changed="(theme) => console.log('테마 바뀜:', theme)" />

  </div>
</template>

<style scoped>
.game-container {
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  user-select : none;
}

.game-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

/* 상단 점수 정렬용 */
.score-row {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
}

.score-box {
  font-size: 1.3rem;
  font-weight: bold;
  background-color: #ffffff;
  color: #333;
  padding: 8px 16px;
  border-radius: 12px;
  border: 2px solid #4fc3f7;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.theme-toggle * {
  user-select: text !important;
}
</style>