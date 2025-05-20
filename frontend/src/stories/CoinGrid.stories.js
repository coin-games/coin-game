import { action } from "@storybook/addon-actions";
import CoinGrid from "../components/CoinGrid.vue";

export default {
	title: "Components/CoinGrid",
	component: CoinGrid,
	argTypes: {
		cols: { control: "number" },
		rows: { control: "number" },
		gameOver: { control: "boolean" },
	},
};

const Template = (args) => ({
	components: { CoinGrid },
	setup() {
		const handleScore = action("update:score");
		const handleTime = action("update:time");
		const handleEnd = action("game-end");

		return {
			args,
			handleScore,
			handleTime,
			handleEnd,
		};
	},
	template: `
    <CoinGrid
      v-bind="args"
      @update:score="handleScore"
      @update:time="handleTime"
      @game-end="handleEnd"
    />
  `,
});

export const Default = Template.bind({});
Default.args = {
	cols: 5,
	rows: 2,
	gameOver: false,
	initialApples: [
		{ number: 2, selected: false, hidden: false },
		{ number: 3, selected: false, hidden: false },
		{ number: 5, selected: false, hidden: false },
		{ number: 1, selected: false, hidden: false },
		{ number: 4, selected: false, hidden: false },
		{ number: 6, selected: false, hidden: false },
		{ number: 2, selected: false, hidden: false },
		{ number: 8, selected: false, hidden: false },
		{ number: 3, selected: false, hidden: false },
		{ number: 7, selected: false, hidden: false },
	],
};
