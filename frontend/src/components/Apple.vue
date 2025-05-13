<template>
  <div
    :class="['coin', { selected, hide: shouldAnimateHide }]"
    v-show="visible"
    @animationend="onAnimationEnd"
  >
    <span class="number">{{ number }}</span>
  </div>
</template>

<script>
export default {
  name: 'Apple',
  props: {
    selected: Boolean,
    hidden: Boolean,
    number: Number,
  },
  data() {
    return {
      visible: true,
      shouldAnimateHide: false,
    };
  },
  watch: {
    hidden(newVal) {
      if (newVal) {
        this.shouldAnimateHide = true;
      }
    },
  },
  methods: {
    onAnimationEnd() {
      if (this.shouldAnimateHide) {
        this.visible = false;
      }
    },
  },
  mounted() {
    const style = getComputedStyle(document.documentElement);
    this.$el.style.setProperty(
      '--coin-bg',
      style.getPropertyValue('--coin-bg')
    );
    this.$el.style.setProperty(
      '--coin-border',
      style.getPropertyValue('--coin-border')
    );
    this.$el.style.setProperty(
      '--coin-text',
      style.getPropertyValue('--coin-text')
    );
  },
};
</script>

<style scoped>
.coin {
  width: 100%;
  height: 100%;
  background: var(
    --coin-bg,
    radial-gradient(circle at 30% 30%, #ffe082, #ffca28)
  );
  border: 3px solid var(--coin-border, #fbc02d);
  border-radius: 50%;
  box-shadow: inset -2px -2px 4px rgba(255, 255, 255, 0.4),
    inset 2px 2px 4px rgba(0, 0, 0, 0.2), 0 0 4px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--coin-text, #fffde7);
  font-weight: bold;
  font-size: 20px;
  position: relative;
  transition: transform 0.2s ease;
}

.coin.selected {
  outline: 2px solid #ffd600;
  transform: scale(1.1);
}

.coin.hide {
  animation: spinOut 0.4s forwards;
}

@keyframes spinOut {
  0% {
    transform: scale(1) rotateY(0deg);
    opacity: 1;
  }
  100% {
    transform: scale(0) rotateY(720deg);
    opacity: 0;
  }
}
</style>
