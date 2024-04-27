import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify);

export default new Vuetify({
    icons: {
        iconfont: 'mdi',
    },
    options: {
        customProperties: true
    },
    theme: {
        themes: {
            light: {

                // Navigation bar, major buttons, active states, footers
                primary: '#00416a',

                // Secondary buttons, hover states, highlights
                secondary: '#819595',

                // secondary2: '#99A69E',
                // secondary3: '#A5AEA2',
                // secondary4: '#b1b6a6',

                // Accent for attention
                accent: '#ef8275',
                // accent2: '#6d4c3d',

                // Background, card colors, text
                neutral: '#F2F2F3',
                neutral2: '#C8CDD0',
                neutral3: '#415058',
                neutral4: '#1F292E',

                // Status colors
                error: '#D41111',
                warning: '#FFAB1A',
                info: '#2196F3',
                success: '#73C322',

            },
        },
    },
});
