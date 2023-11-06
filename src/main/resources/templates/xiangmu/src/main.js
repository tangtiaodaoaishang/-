import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import store from './store'
//引用ajax的request请求第一步
import request from "@/utils/request";
Vue.config.productionTip = false
Vue.use(ElementUI,{size:"mini"});
//引用ajax的request请求第二步
Vue.prototype.request=request
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
