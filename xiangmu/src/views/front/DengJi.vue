<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px;" placeholder="请输入教室名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin:10px 0">
      <el-button type="primary" @click="handleAdd1" v-if="user.role=='ROLE_TEACHER'">新增<i class="el-icon-circle-plus"></i></el-button>
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
        <el-button type="danger" slot="reference" class="mr-5" v-if="user.role==='ROLE_TEACHER'">批量删除<i class="el-icon-remove"></i></el-button>
      </el-popconfirm>

    </div>

    <el-table :data="tableData" border stripe  :header-cell-class-name="'headBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="课程名称" width="100"></el-table-column>
      <el-table-column prop="number" label="最大招收人数" width="120"></el-table-column>
      <el-table-column prop="time" label="每周上课次数"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column label="是否启用多媒体上课">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400px">
        <template slot-scope="scope">
          <el-button type="warning" @click="handleEdit(scope.row)" v-if="user.role=='ROLE_TEACHER'">编辑<i class="el-icon-edit"></i> </el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定?'
              cancel-button-text='在考虑一下'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗?"
              @confirm="handleDelete(scope.row.id)"
          >
            <el-button type="danger" slot="reference"v-if="user.role=='ROLE_TEACHER'">删除<i class="el-icon-remove"></i></el-button>
          </el-popconfirm>


        </template>
      </el-table-column>
    </el-table>

    <!--对话框-->
    <el-dialog title="教室信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="middle">
        <el-form-item label="课程名称" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="最多招收人数" >
          <el-input v-model="form.number" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="每周上课次数" >
          <el-input v-model="form.time" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标" >
          <!--下拉框-->
          <el-select clearable v-model="form.icon" placeholder="请选择图标" style="width:100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{item.name}}
            </el-option>
          </el-select>

        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

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

  </div>
</template>

<script>
export default {
  name: "DengJi",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      dialogFormVisible: false,
      options:{},
      form:[],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      pageNum: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/dengji/page", {
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
      /*fetch("http://localhost:8082/user/page?pageNum="+this.pageNum+"&&pageSize="+this.pageSize+"&&username="+this.username).then(res => res.json()).then(res =>{
        console.log(res)
        this.tableData=res.data;
        this.total=res.total;
      })*/

    },
    //修改
    handleEdit(row){
      this.form=row
      this.dialogFormVisible=true
      //请求图标的数据
      this.request.get("/dengji/icon",{
      }).then(res =>{
        this.options=res.data

      })
    },
    //是否启用
    changeEnable(row) {
      this.request.post("/dengji/update", row).then(res => {
        if (res.code == "200") {
          this.$message.success("操作成功")

        }
      })
    },
    //重置
    reset() {
      this.name = '',
          this.load()
    },
    //单行删除
    handleDelete(id) {
      this.request.delete("/dengji/" + id).then(res => {
        if (res.code == "200") {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
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
      this.request.post("/dengji/del/batch", ids).then(res => {
        if (res.code == "200") {
          this.$message.success("删除多条成功")
          this.load()
        } else {
          this.$message.error("删除多条失败")
        }
      })
    },
    download(url) {
      window.open(url)
    },
    //添加
    save(){
      this.request.post("/dengji/insert",this.form).then(res =>{
        if(res.code=='200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd1(){
      this.dialogFormVisible=true
      this.form = {}
      //请求图标的数据
      this.request.get("/dengji/icon",{
      }).then(res =>{
        this.options=res.data

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
    handleFileUploadSuccess(res) {
      console.log(res)
      this.load()
    }

  }
}
</script>

<style scoped>

</style>
