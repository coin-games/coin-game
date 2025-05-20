import Coin from "../components/Coin.vue";

export default {
	title: "Components/Coin",
	component: Coin,
	argTypes: {
		number: { control: "number" },
		selected: { control: "boolean" },
		hidden: { control: "boolean" },
	},
	decorators: [
		() => ({
			template: '<div style="width: 100px; height: 100px; font-size: 20px;"><story /></div>',
		}),
	],
};

const Template = (args) => ({
	components: { Coin },
	setup() {
		return { args };
	},
	template: '<Coin v-bind="args" />',
});

export const Default = Template.bind({});
Default.args = {
	number: 7,
	selected: false,
	hidden: false,
};

export const Selected = Template.bind({});
Selected.args = {
	number: 3,
	selected: true,
	hidden: false,
};

export const Hidden = Template.bind({});
Hidden.args = {
	number: 5,
	selected: false,
	hidden: true,
};
