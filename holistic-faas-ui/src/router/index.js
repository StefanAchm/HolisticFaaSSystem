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
        path: '/workflows',
        name: 'workflows',
        component: () => import('../views/WorkflowsView.vue')
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
    },
    {
        path: '/workflows/:id',
        name: 'workflow',
        component: () => import('../views/WorkflowDetailsView.vue')
    },
    {
        path: '/workflows/:id/deployments/:deploymentId',
        name: 'deployments',
        component: () => import('../views/WorkflowDeploymentView.vue')
    },
    {
        path: '*',
        beforeEnter: (to, from, next) => {
            document.title = 'home'
            next({name: 'home'})
        }
    }

]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {

    store.commit('initStore');

    const isAuthenticated = store.getters["isAuthenticated"]

    document.title = to.name

    if (to.name === 'login' && !isAuthenticated) {
        next()
    } else if (to.name === 'login' && isAuthenticated) {
        document.title = 'home'
        next({name: 'home'})
    } else if (!isAuthenticated) {
        document.title = 'login'
        next({name: 'login'})
    } else {
        next()
    }
})

export default router
