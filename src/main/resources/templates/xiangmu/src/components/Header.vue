<template>
  <div style="line-height: 60px;display: flex">
  <div style="flex:1;">
    <span :class="collapseBtnClass" style="cursor: pointer;font-size: 18px" @click="callapse"></span>
    <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px">
      <el-breadcrumb-item :to="{path:'/home'}">首页</el-breadcrumb-item>
      <el-breadcrumb-item >{{currentPathName}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-dropdown style="width: 70px;cursor: pointer">
    <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>

    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item style="font-size: 14px;padding: 5px 0">
        <router-link to="/person" style="text-decoration: none">个人信息</router-link>
      </el-dropdown-item>
      <el-dropdown-item style="font-size: 14px;padding: 5px 0">
<!--<router-link to="/login" style="text-decoration: none">退出登录</router-link>-->
        <span  style="text-decoration: none" @click="logout">退出登录</span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header.vue",
  props:{
    collapseBtnClass:String,
    callapse: Function,
    user:Object

  },
  computed:{
    currentPathName(){
      return this.$store.state.currentPathName;  //需要监听的数据
    }
  },
  data(){
    return{

    }
  },
  methods:{
    //退出
    logout(){
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    }
  },
  //监听路由的变化
  watch:{
    '$route':function (){
      //取出route里面设置的当前路由信息
      this.currentPathName= localStorage.getItem("currentPathName")
    },

  }


}
</script>

<style scoped>

</style>