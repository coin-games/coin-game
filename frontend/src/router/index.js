import { createRouter, createWebHistory } from "vue-router";

import Game from "@/pages/Game.vue";

const router = createRouter({
	history: createWebHistory(),
	routes: [{ path: "/", name: "game", component: Game }],
});

export default router;
