<template>
<div>

  <div style="margin: 10px 0">
    <el-input style="width: 200px;" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
    <el-input style="width: 200px;" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
    <el-input style="width: 200px;" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button  type="warning" @click="reset">重置</el-button>
  </div>
  <div style="margin:10px 0">
    <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus"></i></el-button>
    <el-popconfirm
        class="ml-5"
        confirm-button-text='确定?'
        cancel-button-text='在考虑一下'
        icon="el-icon-info"
        icon-color="red"
        title="您确定要批量删除吗?"
        @confirm="delBatch"
    >
      <!--slot="reference" reference触发Popconfirm(气泡框)显示的HTML 元素-->
      <el-button type="danger" slot="reference" class="mr-5">批量删除<i class="el-icon-remove"></i></el-button>
    </el-popconfirm>


    <el-upload action="http://localhost:8082/user/import" :show-file-list="false" accept="xlsx" :on-success="handleimportSuccess" style="display:inline-block">
    <el-button type="primary" @click="" style="margin-right: 10px">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp">导出<i class="el-icon-top"></i></el-button>
  </div>
  <!--@selection-change="handleSelectionChange" 是table表格中多选类型的方法,其中参数val用来存储多选框的数据,在官网该数据叫做multipleSelection-->
  <el-table :data="tableData" border stripe :header-cell-class-name="headBg"  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="username" label="用户名" width="140"></el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="电话"></el-table-column>
    <el-table-column prop="address" label="地址"></el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button type="warning" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i> </el-button>

        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定?'
            cancel-button-text='在考虑一下'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗?"
            @confirm="handleDelete(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除<i class="el-icon-remove"></i></el-button>
        </el-popconfirm>


      </template>
    </el-table-column>
  </el-table>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
  <!--对话框-->
  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="100px" size="middle">
      <el-form-item label="用户名" >
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称" >
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" >
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" >
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址" >
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "User.vue",

  data(){
    return{
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:10, //默认的每页显示条数
      username:'',
      email:'',
      address:'',
      dialogFormVisible: false,
      form:{

      },
      multipleSelection: [], //存储多选框类型的表格数据
      headBg:'headBg'
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("http://localhost:8082/user/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          email:this.email,
          address:this.address
        }
      }).then(res =>{
        console.log(res)
        this.tableData=res.data.records
        this.total=res.data.total

      })
      /*fetch("http://localhost:8082/user/page?pageNum="+this.pageNum+"&&pageSize="+this.pageSize+"&&username="+this.username).then(res => res.json()).then(res =>{
        console.log(res)
        this.tableData=res.data;
        this.total=res.total;
      })*/

    },
    //重置
    reset(){
      this.username='',
          this.email='',
          this.address='' ,
          this.load()
    },
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
    },
    //修改
    handleEdit(row){
      this.form=row
      this.dialogFormVisible=true
    },
    //添加
    save(){
      this.request.post("http://localhost:8082/user/insert",this.form).then(res =>{
        if(res.code=="200"){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    //单行删除
    handleDelete(id){
      this.request.delete("http://localhost:8082/user/"+id).then(res =>{
        if(res.code=="200"){
          this.$message.success("删除单条数据成功")
          this.load()
        }else {
          this.$message.error("删除单条数据失败")
        }
      })
    },
    //批量删除
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection=val

    },
    //批量删除(注意批量删除接口不支持@DeleteMapper,应该使用@PostMapping
    delBatch(){
      let ids=this.multipleSelection.map(v => v.id) //[{}.{},{}] => [1,2,3] 将json数组对象转为普通int类型数组
      this.request.post("http://localhost:8082/user/del/batch",ids).then(res=>{
        if(res.code=="200"){
          this.$message.success("删除多条数据成功")
          this.load()
        }else {
          this.$message.error("删除多条数据失败")
        }
      })
    },
    //导出
    exp(){
      window.open("http://localhost:8082/user/export")
    },
    //导入成功钩子函数
    handleimportSuccess(){
      this.$message.success("导入成功")
      this.load();
    },
    //分页时触发页面数据刷新方法
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum=pageNum
      this.load()
    }

  }

}
</script>

<style>
.headBg{
  background-color: #cccccc !important;
}
</style>