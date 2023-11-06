<template>
  <div>
<div>
  <el-carousel height="650px" :interval="5000">
    <el-carousel-item v-for="item in imgs" :key="item">
     <img :src="item.url" alt="" style="width: 100%">
    </el-carousel-item>
  </el-carousel>
</div>
  <div style="margin: 10px 0">
    <el-row :gutter="13">
      <el-col :span="6" v-for="item in files" :key="item.id" style="margin-bottom: 10px">
        <el-card>
        <img :src="item.url" alt="" style="width: 100%;">
        <div style="padding: 10px 0">
          <span style="color: #FC466B;padding: 10px 0">{{item.name}}</span>
          <div><el-button type="primary" style="margin: 10px 0">购买</el-button></div>
        </div>
      </el-card>
      </el-col>
    </el-row>
  </div>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data(){
    return{
      imgs:[
        {
          url:require("../../assets/1.png")
        },
        {
          url:require("../../assets/2.png")
        },
        {
          url:require("../../assets/3.png")
        }

      ],
      files:[]
    }
  },
  created() {
    this.request.get("/echarts/front/all").then(res =>{
      console.log(res)
      this.files=res.data.filter(v => v.type==='png'||v.type==='jpg'||v.type==='docx'||v.type==='xlsx')
    })
  },
  methods:{

  }
}
</script>

<style scoped>

</style>