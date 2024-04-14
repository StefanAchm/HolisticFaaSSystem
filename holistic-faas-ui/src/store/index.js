import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({

    state: {
        token: null,
        username: null,
        userId: null
    },

    mutations: {

        initStore(state) {
            state.token = localStorage.getItem('token');
            state.username = localStorage.getItem('username');
            state.userId = localStorage.getItem('userId');
        },

        setToken(state, token) {
            state.token = token;
        }

    },

    actions: {
        login({commit}, data) {
            commit('setToken', data.token);
            // Optionally, store the token in localStorage for persistent login
            localStorage.setItem('token', data.token);
            localStorage.setItem('username', data.user.username);
            localStorage.setItem('userId', data.user.id);
        },
        logout({commit}) {
            commit('setToken', null);
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            localStorage.removeItem('userId');
        }
    },

    getters: {
        isAuthenticated(state) {
            return state.token != null;
        },

        getUser(state) {
            return {
                username: state.username,
                id: state.userId
            };
        }


    }


});
