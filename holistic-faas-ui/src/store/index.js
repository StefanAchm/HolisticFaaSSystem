import Vue from 'vue';
import Vuex from 'vuex';
// import auth from './modules/auth'; // Import the auth module

Vue.use(Vuex);

export default new Vuex.Store({

    state: {
        token: null,
    },

    mutations: {
        setToken(state, token) {
            state.token = token;
        }
    },
    actions: {
        login({commit}, token) {
            commit('setToken', token);
            // Optionally, store the token in localStorage for persistent login
            localStorage.setItem('token', token);
        },
        logout({commit}) {
            commit('setToken', null);
            localStorage.removeItem('token');
        }
    },

    getters: {
        isAuthenticated(state) {
            return state.token != null;
        }
    }


});
