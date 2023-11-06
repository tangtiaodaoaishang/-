<template>
<div>
  <div style="padding: 10px">
    <el-card>
      <div v-for="item in videos" :key="item.id" style="margin: 10px 0;padding: 10px 0;color: #666;border-bottom: 1px dashed #ccc">
        <span style="font-size: 18px;cursor: pointer" class="item">{{item.username}}</span>
        <span style="float: right;margin-left:30px;font-size: 12px;margin-top: 10px">{{item.role}}</span>
        <span style="float: right;font-size: 12px;margin-top: 10px">{{item.nickname}}</span>
      </div>
    </el-card>
  </div>
</div>
</template>

<script>
export default {
  name: "shop",
  data(){
    return{
      videos:[]
    }
  },
  created() {
    this.request.get("/user/front/page").then(res=>{
      console.log(res.data)
      this.videos=res.data;
    })
  },
}
</script>

<style scoped>

</style>