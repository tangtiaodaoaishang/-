<template>

    <el-container style="min-height: 100vh">
<!--菜单布局-->
      <el-aside :width="asideWidth+'px'" style="background-color: rgb(238, 241, 246);height: 100vh;box-shadow:2px 0 6px rgba(0 21 41 0.35)" overflow="hidden">
<!--菜单栏-->
          <Aside :isCollapse="isCollapse" :showText="showText"></Aside>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid #ccc;">
              <Header :callapse="callapse"  :collapseBtnClass="collapseBtnClass" :user="user"></Header>
        </el-header>

        <el-main>
<!--表示当前页面的子路由会在router-view里面展示-->
           <router-view @refreshUser="getUser"></router-view>
        </el-main>
      </el-container>
    </el-container>

</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";



export default {
  name: 'HomeView',

  data(){

    return{
      msg: "牛逼plus",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse:false,
      asideWidth:200,
      showText:true,
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{}
    }
  },

  components:{
    Header,
    Aside
  },
  methods: {
    callapse() { //点击收缩按钮触发菜单栏收缩/展开
      this.isCollapse = !this.isCollapse;
      //如果菜单栏收缩
      if (this.isCollapse) {
        this.asideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.showText = false
        //如果菜单栏展开
      } else {
        this.asideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.showText = true
      }

    },
    getUser(){
      //从后台获取数据
      this.request.get("/user/username/"+this.user.username).then(res=>{})
       //重新赋值后台最新的user数据
      this.user=res.data
    }
  }
}
</script>

<style>

</style>
