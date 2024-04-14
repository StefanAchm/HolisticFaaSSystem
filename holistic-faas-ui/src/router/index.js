import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from "@/views/HomeView";
import store from "../store";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/functions',
        name: 'functions',
        component: () => import('../views/FunctionsView.vue')
    },
    {
        path: '/users',
        name: 'users',
        component: () => import('../views/UserView.vue')
    },
    {
        path: '/profile',
        name: 'profile',
        component: () => import('../views/ProfileView.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/LoginView.vue')
    }
]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {

    store.commit('initStore');

    const isAuthenticated = store.getters["isAuthenticated"]

    if (to.name === 'login') {
        next()
    } else if (!isAuthenticated) {
        next({name: 'login'})
    } else {
        next()
    }
})

export default router
