import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from "@/views/HomeView";

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
        component: () => import('../views/FunctionView.vue')
    },
    {
        path: '/users',
        name: 'users',
        component: () => import('../views/UserView.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
