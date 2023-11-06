<template>
 <div>
<!--头部-->
   <div style="display: flex;height: 60px;line-height: 60px;border-bottom:1px solid #ccc">
     <div style="width: 300px;display: flex;padding-left: 30px">
       <div width="60px">
       <img src="../../assets/edg.png" alt="" style="width: 50px;position: relative;top:5px;right: 0">
       </div>
       <div style="flex: 1;margin-left: 10px" >欢迎来到xxx系统</div>
     </div>
     <div style="flex:1">
<!--导航菜单-->
       <el-menu :default-active="'1'" class="el-menu-demo" mode="horizontal" router>
         <el-menu-item index="/front/home">主页</el-menu-item>
         <el-menu-item index="/front/video">视频播放</el-menu-item>
         <el-menu-item index="/front/article">文章列表</el-menu-item>
         <el-submenu index="2">
           <template slot="title">总体</template>
           <el-menu-item index="2-2">选项2</el-menu-item>
           <el-menu-item index="2-3">选项3</el-menu-item>
           <el-menu-item index="/front/shop">用户详情</el-menu-item>
           <el-submenu index="2-4">
             <template slot="title">选项4</template>
             <el-menu-item index="2-4-1">选项1</el-menu-item>
             <el-menu-item index="2-4-2">选项2</el-menu-item>
             <el-menu-item index="2-4-3">选项3</el-menu-item>
           </el-submenu>
         </el-submenu>
         <el-menu-item index="3" disabled>消息中心</el-menu-item>
         <el-menu-item index="4">订单管理</el-menu-item>
       </el-menu>
     </div>
     <div style="width: 300px">
       <div v-if="!user.username" style="text-align: right;padding-right: 30px">
           <el-button @click="$router.push('/login')">登录</el-button>
         <el-button  @click="$router.push('/register')">注册</el-button>
       </div>
       <div v-else>
         <el-dropdown style="width: 200px;cursor: pointer;text-align: right">
           <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>

           <el-dropdown-menu slot="dropdown">
             <el-dropdown-item style="font-size: 14px;padding: 5px 0">
               <router-link to="/front/person" style="text-decoration: none">个人信息</router-link>
             </el-dropdown-item>
             <el-dropdown-item style="font-size: 14px;padding: 5px 0">
               <!--<router-link to="/login" style="text-decoration: none">退出登录</router-link>-->
               <span  style="text-decoration: none" @click="logout">退出登录</span>
             </el-dropdown-item>
           </el-dropdown-menu>
         </el-dropdown>
       </div>

     </div>
   </div>

  <div style="width: 1000px;margin:0 auto">
    <router-view></router-view>
  </div>
 </div>
</template>

<script>
export default {
  name: "Front",
  data(){
    return {
      user: localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{}
    }
    },
  created() {
  },
  methods:{
    logout(){
      this.$store.commit("logout");
      this.$message.success("退出成功")
    }

}

}
</script>

<style scoped>
.item {
  display: inline-block;
  width: 100px;
  color: #3F5EFB;
  cursor: pointer;
  text-align: center;
}
.item:hover{
  background-color: #FC466B;
}
.item a{
  color: #E6A23C;
  text-decoration: none;
}
</style>