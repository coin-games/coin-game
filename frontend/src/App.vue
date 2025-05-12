<template>
  <RouterView />
</template>

<script setup>
import { RouterView } from 'vue-router';
import { useHead } from '@vueuse/head';

useHead({
  title: '사과 게임 | 숫자 10 맞추기 퍼즐 코인 게임',
  meta: [
    {
      name: 'description',
      content:
        '사과를 드래그해서 숫자 합이 10이 되면 사라지는 중독성 퍼즐 게임! 코인을 모으고 랭킹에 도전하세요.',
    },
    {
      name: 'keywords',
      content:
        '사과 게임, 숫자 게임, 10 만들기, 퍼즐 게임, 코인 게임, 뇌운동 게임, 중독성 게임',
    },
    { property: 'og:title', content: '사과 게임 - 숫자 10 맞추기 퍼즐' },
    {
      property: 'og:description',
      content:
        '사과를 드래그해서 합이 10이 되면 사라진다! 점수를 올리고 랭킹 1위에 도전해보자!',
    },
    { property: 'og:type', content: 'website' },
    { property: 'og:url', content: 'https://coingame0.netlify.app/' },
    { name: 'twitter:card', content: 'summary_large_image' },
  ],
  link: [{ rel: 'icon', href: '/favicon.svg', type: 'image/svg+xml' }],
});
</script>
