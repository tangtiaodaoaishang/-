<template>
<div>

  <div style="margin: 10px 0">
    <el-input style="width: 200px;" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
 <!--   <el-input style="width: 200px;" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
    <el-input style="width: 200px;" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button  type="warning" @click="reset">重置</el-button>
  </div>
  <div style="margin:10px 0">
<el-button type="primary" @click="handleAdd1">新增<i class="el-icon-circle-plus"></i></el-button>
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

  </div>
  <!--@selection-change="handleSelectionChange" 是table表格中多选类型的方法,其中参数val用来存储多选框的数据,在官网该数据叫做multipleSelection-->
  <el-table :data="tableData" border stripe :header-cell-class-name="headBg" row-key="id" default-expand-all @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="名称" ></el-table-column>
    <el-table-column prop="path" label="路径" ></el-table-column>
    <el-table-column prop="pagePath" label="页面路径" ></el-table-column>
    <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
      <template slot-scope="scope">
        <i :class="scope.row.icon"></i>
      </template>
    </el-table-column>
    <el-table-column prop="description" label="描述" ></el-table-column>
    <el-table-column label="操作" width="280">
      <template slot-scope="scope">
        <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid&&!scope.row.path">新增子菜单<i class="el-icon-plus"></i> </el-button>
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

  <!--对话框-->
  <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="100px" size="middle">
      <el-form-item label="名称" >
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="路径" >
        <el-input v-model="form.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="页面路径" >
        <el-input v-model="form.pagePath" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标" >
        <!--下拉框-->
         <el-select clearable v-model="form.icon" placeholder="请选择图标" style="width:100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{item.name}}
            </el-option>
         </el-select>

      </el-form-item>
      <el-form-item label="描述" >
        <el-input v-model="form.description" autocomplete="off"></el-input>
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
      name:'',
      dialogFormVisible: false,
      form:[],
      multipleSelection: [], //存储多选框类型的表格数据
      headBg:'headBg',
      options:{},
      item:{},
      pagePath:{}

    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("http://localhost:8082/menu",{
        params:{
          name:this.name,

        }
      }).then(res =>{
        console.log(res)
        this.tableData=res.data

      })
      /*fetch("http://localhost:8082/user/page?pageNum="+this.pageNum+"&&pageSize="+this.pageSize+"&&username="+this.username).then(res => res.json()).then(res =>{
        console.log(res)
        this.tableData=res.data;
        this.total=res.total;
      })*/

    },
    //重置
    reset(){
           this.name='',
          this.description='',
          this.load()
    },
    handleAdd(pid){
      this.dialogFormVisible=true
      this.form={}
      if(pid){
        this.form.pid=pid;
      }
    },
    //修改
    handleEdit(row){
      this.form=row
      this.dialogFormVisible=true
      //请求图标的数据
      this.request.get("/menu/icon",{
      }).then(res =>{
        this.options=res.data

      })
    },
    //添加
    save(){
      this.request.post("http://localhost:8082/menu/insert",this.form).then(res =>{
        if(res.code=='200'){
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
      this.request.delete("http://localhost:8082/menu/"+id).then(res =>{
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
      this.request.post("http://localhost:8082/menu/del/batch",ids).then(res=>{
        if(res.code=="200"){
          this.$message.success("删除多条数据成功")
          this.load()
        }else {
          this.$message.error("删除多条数据失败")
        }
      })
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
    },
    handleAdd1(){
      this.dialogFormVisible=true
      this.form = {}
      //请求图标的数据
      this.request.get("/menu/icon",{
      }).then(res =>{
        this.options=res.data

      })
    }

  }

}
</script>

<style>
.headBg{
  background-color: #cccccc !important;
}
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>