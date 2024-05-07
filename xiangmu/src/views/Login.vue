<template>
 <div class="wrapper">
   <div style="margin:200px auto;background-color:#fff;width:350px;height:500px;padding: 20px;border-radius: 10px">
     <div style="margin: 20px 0;text-align: center;font-size:24px"><b>登 录</b></div>
     <el-form :model="user" :rules="rules" ref="userForm">
       <el-form-item prop="username">
         <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
       </el-form-item>

       <el-form-item prop="phone" >
         <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-phone" v-model="user.phone"></el-input>
       </el-form-item>
       <el-form-item prop="code" >
         <el-input  size="medium" prefix-icon="el-icon-chat-round" v-model="user.code" ></el-input>
         <el-button @click="setCode()" style="float: left">生成验证码</el-button>
           <el-button @click="removeCode()" style="float: right">删除验证码</el-button>
       </el-form-item>

       <el-form-item prop="password">
         <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
       </el-form-item>

       <el-form-item style="margin: 10px 0;text-align: right">
         <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
         <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
       </el-form-item>
     </el-form>

   </div>
 </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data(){
    return {
      user: {},

      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {min: 11, max: 11, message: '长度在11个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    setCode(){
      this.request.post("/user/code/"+this.user.phone,this.user).then(res=>{
        if(res.code==200){
          alert("已成功发送验证码,注意接收")
        }else{
          this.$message.error(res.msg)
        }
    })
    },
    removeCode(){
      this.user.code=''
    },
    login(){
      //登录校验
      this.$refs['userForm'].validate((valid) => {
        if (valid) { //表单校验合法
          alert("用户名和手机号和密码符合匹配规则")
          this.request.post("/user/login/"+this.user.phone,this.user).then(res=>{
            if(res.code=="200"){
              //JSON.stringify(res.data):把json数据对象转为json字符串数据
              localStorage.setItem("user",JSON.stringify(res.data));  //存储用户信息到浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus)); //存储菜单路由信息到浏览器
              //动态设置当前用户的路由
              setRoutes()
              if(res.data.role==='ROLE_USER'){
                this.$router.push("/front/home")
              }else {
                //跳转到首页路由
                this.$router.push("/")
              }

              this.$message.success(res.msg)
            }else{
              this.$message.error(res.msg)
            }
          })
          //表单校验不合法
        } else {
          alert("用户名和手机号和密码不符合匹配规则")
          return false;
        }
      });


    }
  }

}
</script>

<style>
  .wrapper{
    height: 100vh;
    /*颜色渐变(从左上到右下)*/
    background-image:linear-gradient(to bottom right,#FC466B,#3F5EFB);
    overflow: hidden;
  }
</style>
