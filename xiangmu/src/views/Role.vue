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

  </div>
  <!--@selection-change="handleSelectionChange" 是table表格中多选类型的方法,其中参数val用来存储多选框的数据,在官网该数据叫做multipleSelection-->
  <el-table :data="tableData" border stripe :header-cell-class-name="headBg"   @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="名称" ></el-table-column>
    <el-table-column prop="flag" label="唯一标识" ></el-table-column>
    <el-table-column prop="description" label="描述" ></el-table-column>
    <el-table-column label="操作" width="280">
      <template slot-scope="scope">
        <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
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
  <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="100px" size="middle">
      <el-form-item label="名称" >
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="唯一标识" >
        <el-input v-model="form.flag" autocomplete="off"></el-input>
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
  <!--对话框-->
  <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
    <el-tree
        :props="props"
        :data="menuData"
        show-checkbox
        ref="tree"
        node-key="id"
        :default-expanded-keys="expends"
        :default-checked-keys="checks">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon"></i> {{ data.name }}</span>
        </span>
          </el-tree>
    <div slot="footer" class="dialog-footer">
      <el-button @click="menuDialogVis = false">取 消</el-button>
      <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "User.vue",

  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10, //默认的每页显示条数
      name: '',
      dialogFormVisible: false,
      menuDialogVis: false,
      form: {},
      multipleSelection: [], //存储多选框类型的表格数据
      headBg: 'headBg',
      menuData: [],
      props:{
        label: 'name',
      },
      expends:[],
      checks:[],
      roleId:0,
      roleFlag:''
    }
  },


  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("http://localhost:8082/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,

        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total

      })


    },
    //重置
    reset() {
      this.name = '',
          this.description = '',
          this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    //修改
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    //添加
    save() {
      this.request.post("http://localhost:8082/role/insert", this.form).then(res => {
        if (res.code == "200") {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    //单行删除
    handleDelete(id) {
      this.request.delete("http://localhost:8082/role/" + id).then(res => {
        if (res.code == "200") {
          this.$message.success("删除单条数据成功")
          this.load()
        } else {
          this.$message.error("删除单条数据失败")
        }
      })
    },
    //批量删除
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val

    },
    //批量删除(注意批量删除接口不支持@DeleteMapper,应该使用@PostMapping
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id) //[{}.{},{}] => [1,2,3] 将json数组对象转为普通int类型数组
      this.request.post("http://localhost:8082/role/del/batch", ids).then(res => {
        if (res.code == "200") {
          this.$message.success("删除多条数据成功")
          this.load()
        } else {
          this.$message.error("删除多条数据失败")
        }
      })
    },


    //分页时触发页面数据刷新方法
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    selectMenu(role) {
      this.menuDialogVis = true
      this.roleId = role.id
      this.roleFlag=role.flag
      //请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data
        //把后台返回的菜单数据处理成id数组
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/"+this.roleId).then(res => {
        //先渲染弹窗的元素
        this.menuDialogVis=true
        this.checks=res.data
        this.request.get("/menu/ids").then(r=>{
            const ids=r.data
            ids.forEach(id=>{
              if(!this.checks.includes(id)){
                //可能会报错
                this.$refs.tree.setChecked(id,false)
              }
            })
      })


      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res => {
         if(res.code=="200"){
           this.$message.success("绑定成功")
           this.menuDialogVis=false
           //操作管理员角色后需要重新登录
           if(this.roleFlag=="ROLE_ADMIN"||this.roleFlag=="ROLE_TEACHER"){
             this.$store.commit("logout")
           }
         }else{
           this.$message.error(res.msg)
         }
      })

    },

}
}
</script>

<style>
.headBg{
  background-color: #cccccc !important;
}
</style>