import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manager.vue'
import store from '../store'
Vue.use(VueRouter)

//静态路由
const routes = [

    // 登录页面路由
  {
    path: '/login',
    name: 'login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/Login.vue')
  },
 /*   //文件管理页面
  {
    path: '/file',
    name: 'file',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/File.vue')
  },*/
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
  },
  {
    path: '/404',
    name: '404',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/404.vue')
  },
  {
    path: '/front',
    name: 'front',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/front/Front'),
    children:[
      {
        path:"home",
        name:"FrontHome",
        component:() => import("../views/front/Home")
      },
      {
        path:"shop",
        name:"FrontShop",
        component:() => import("../views/front/Shop")
      },
      {
        path:"person",
        name:"person",
        component:() => import("../views/front/Person")
      },
      {
        path:"video",
        name:"video",
        component:() => import("../views/front/Video")
      },
      {
        path:"videoDetail",
        name:"videoDetail",
        component:() => import("../views/front/VideoDetail")
      },
      {
        path:"article",
        name:"FrontArticle",
        component:() => import("../views/front/Article")
      },
      {
        path:"articleDetail",
        name:"FrontArticleDetail",
        component:() => import("../views/front/ArticleDetail")
      },
      {
        path:"dengji",
        name:"FrontDengji",
        component:() => import("../views/front/DengJi")
      },

    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//提供重置路由方法
export const resetRouter=()=>{
  router.matcher=new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

//动态路由
//注意:这里刷新页面会导致页面动态路由重置
export const setRoutes=()=>{
  const storeMenus=localStorage.getItem("menus");
  if(storeMenus){
    //获取当前的路由对象的名称数组
    const currentRouteNames=router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes("Manage")){
      //拼装动态理由
      const manageRoute={path: '/',name:'Manage', component: () => import('../views/Manager.vue'), redirect:"/home", children:[
          //个人信息页面路由
          {
            path: '/person',
            name: 'person',
            // route level code-splitting
            // this generates a separate chunk (about.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('../views/Person.vue')
          }
        ]}
      const menus=JSON.parse(storeMenus); //将数据转换为 JavaScript 对象
      menus.forEach(item =>{
        if(item.path){ //当前仅当path不为空的时候才去设置路由
          let itemMenu={path: item.path.replace("/",""),name:item.name,component: () => import('../views/'+item.pagePath+'.vue')}
          manageRoute.children.push(itemMenu)
        }else if(item.children.length){
          item.children.forEach(item=>{
            if(item.path){
              let itemMenu={path: item.path.replace("/",""),name:item.name,component: () => import('../views/'+item.pagePath+'.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }

      })

      //动态添加路由到现在的对象路由中去
      //这里只有路由不包含Manage才会执行,在添加新的页面路由是会包含之前的路由信息,因为之前的路由存在Manage,所以不会执行该代码块
      //因此在添加菜单路由后退到登陆页面需要先重置之前的路由
      router.addRoute(manageRoute)
    }

  }
}
//刷新页面就会重新set页面动态路由
setRoutes()



// 路由守卫
router.beforeEach((to, from, next)=>{
  localStorage.setItem("currentPathName",to.name) //设置当前路由的名称
  store.commit("setPath") //触发store的数据更新

  //未找到路由的情况
  if(!to.matched.length){
    const storeMenus=localStorage.getItem("menus")
    if(storeMenus){
      next("/404")
    }else{
      //跳回登录页面
      next("/login")
    }
  }
  //其他的情况就放行
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
