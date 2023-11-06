import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manager.vue'
import store from '../store'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manager.vue'),
    redirect:"/home",
    children:[
      {
        path: 'home',
        name: '主页',
        component: () => import('../views/Home.vue'),
      },
        {
        path: 'user',
        name: '用户管理',
        component: () => import('../views/User.vue')
      },
      // 个人信息页面路由
      {
        path: 'person',
        name: '个人信息',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Person.vue')
      },
      // 文件管理页面路由
      {
        path: 'file',
        name: '文件管理',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/File.vue')
      }
    ]
  },
    // 登录页面路由
  {
    path: '/login',
    name: 'login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/Login.vue')
  },
  // 注册页面路由
  {
    path: '/register',
    name: 'register',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/Register.vue')
  },

  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next)=>{
  localStorage.setItem("currentPathName",to.name) //设置当前路由的名称
  store.commit("setPath") //触发store的数据更新
  next() //放行路由
})
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace (location) {
  return originalReplace.call(this, location).catch(err => err)
}

export default router
