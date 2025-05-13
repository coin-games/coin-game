<template>
  <div class="theme-toggle">
    <button @click="showThemeSelect = !showThemeSelect">🎨</button>
    <div v-if="showThemeSelect" class="theme-options">
      <label v-for="(theme, key) in themes" :key="key">
        <input type="radio" :value="key" v-model="selectedTheme" />
        {{ key }}
      </label>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineEmits, onMounted } from 'vue';

const emit = defineEmits(['theme-changed']);
const showThemeSelect = ref(false);
const selectedTheme = ref('light');

const themes = {
  lightMode: {
    '--bg-color': '#f0f9ff',
    '--primary-color': '#4fc3f7',
    '--text-color': '#333',
    '--coin-bg': 'radial-gradient(circle at 30% 30%, #ffe082, #ffca28)',
    '--coin-border': '#fbc02d',
    '--coin-text': '#fffde7',
  },
  darkMode: {
    '--bg-color': '#1e1e1e',
    '--primary-color': '#9e9e9e',
    '--text-color': '#f0f0f0',
    '--coin-bg': 'radial-gradient(circle at 30% 30%, #757575, #424242)',
    '--coin-border': '#9e9e9e',
    '--coin-text': '#eeeeee',
  },
  onlyText: {
    '--bg-color': '#fffde7',
    '--primary-color': '#ffeb3b',
    '--text-color': '#333',
    '--coin-bg': 'radial-gradient(circle at 30% 30%, #fff59d, #fbc02d)',
    '--coin-border': '#f9a825',
    '--coin-text': '#212121',
  },
};

watch(selectedTheme, (themeKey) => {
  const theme = themes[themeKey];
  Object.entries(theme).forEach(([key, value]) => {
    document.documentElement.style.setProperty(key, value);
  });
  localStorage.setItem('selected-theme', themeKey);
  emit('theme-changed', themeKey);
});

onMounted(() => {
  const savedTheme = localStorage.getItem('selected-theme');
  if (savedTheme && themes[savedTheme]) {
    selectedTheme.value = savedTheme;
  }
});
</script>

<style scoped>
.theme-toggle {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1001;
}

.theme-toggle > button {
  background-color: var(--primary-color, #4fc3f7);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 20px;
  width: 40px;
  height: 40px;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.theme-options {
  background: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  margin-top: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.theme-options label {
  display: block;
  margin-bottom: 6px;
  font-size: 0.9rem;
  color: black;
}
</style>
