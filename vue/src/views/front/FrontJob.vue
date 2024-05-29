<template>
  <div>
<!--    <div style="margin: 10px 0">-->
<!--      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>-->
<!--      &lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>&ndash;&gt;-->
<!--      &lt;!&ndash;      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>&ndash;&gt;-->
<!--      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>-->
<!--      <el-button type="warning" @click="reset">重置</el-button>-->
<!--    </div>-->

    <div style="margin: 10px 0">
<!--      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>-->

<!--      <el-popconfirm-->
<!--          class="ml-5"-->
<!--          confirm-button-text='确定'-->
<!--          cancel-button-text='我再想想'-->
<!--          icon="el-icon-info"-->
<!--          icon-color="red"-->
<!--          title="您确定批量删除这些数据吗？"-->
<!--          @confirm="delBatch"-->
<!--      >-->
<!--        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>-->
<!--      </el-popconfirm>-->
      <!-- <el-upload action="http://localhost:9999/paper/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" stripe size="medium"  @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="作业名称"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
<!--      <el-table-column prop="courseid" label="课程id"></el-table-column>-->
      <el-table-column label="所属课程">
        <template v-slot="scope">
          <span v-if="courses && courses.length">{{ courses.find(v => v.id === scope.row.courseid).name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="作业">
        <template v-slot="scope">
          <el-button type="primary" @click="viewJob(scope.row.id)">查看作业</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="380" align="center">
        <template slot-scope="scope">
          <el-button type="primary" plain @click="handlePaper(scope.row.id)">完成作业 <i class="el-icon-document"></i></el-button>

          <el-button type="success" @click="viewPaper(scope.row.id)">查看解析 <i class="el-icon-edit"></i></el-button>
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


    <el-dialog title="作业" :visible.sync="dialogFormVisible2" width="60%" :close-on-click-modal="false">
      <el-card>
        <div v-for="(item, index) in questiones" :key="item.id"  style="margin: 20px 0">
          <div style="margin: 10px 0; font-size: 20px"><span>{{ index+1 }}.</span> {{ item.name }}
            <span style="font-size: 14px" v-if="item.type === 1">（选择题）</span>
            <span style="font-size: 14px"  v-if="item.type === 2">（判断题）</span>
            <span style="font-size: 14px"  v-if="item.type === 3">（问答题）</span>
          </div>
          <div v-if="item.type === 1" style="margin: 10px">
            <span style="margin-right: 20px">A. {{ item.a }}</span>
            <span  style="margin-right: 20px">B. {{ item.b }}</span>
            <span  style="margin-right: 20px">C. {{ item.c }}</span>
            <span  style="margin-right: 20px">D. {{ item.d }}</span>
          </div>
          <div style="margin: 10px">
            答案： {{ item.answer }}
          </div>
          <div style="margin: 10px">
            解析： {{ item.detail }}
          </div>
        </div>
      </el-card>
    </el-dialog>

    <el-dialog title="作业详情" :visible.sync="dialogFormVisible4" width="60%" :close-on-click-modal="false">
      <el-card>
        <div v-for="(item, index) in questiones" :key="item.id"  style="margin: 20px 0">
          <div style="margin: 10px 0; font-size: 20px"><span>{{ index+1 }}.</span> {{ item.name }}
            <span style="font-size: 14px" v-if="item.type === 1">（选择题）</span>
            <span style="font-size: 14px"  v-if="item.type === 2">（判断题）</span>
            <span style="font-size: 14px"  v-if="item.type === 3">（问答题）</span>
          </div>
          <div v-if="item.type === 1" style="margin: 10px">
            <span style="margin-right: 20px">A. {{ item.a }}</span>
            <span  style="margin-right: 20px">B. {{ item.b }}</span>
            <span  style="margin-right: 20px">C. {{ item.c }}</span>
            <span  style="margin-right: 20px">D. {{ item.d }}</span>
          </div>
        </div>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "FrontJob",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      form1: {},
      form2: {},
      dialogFormVisible: false,
      dialogFormVisible1: false,
      dialogFormVisible2: false,
      dialogFormVisible3: false,
      dialogFormVisible4: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      courses: [],
      questiones: [],
      courseId: null,
      type: null,
      paperQuestions: [],
      // paperQuestionsOrigin: []
    }
  },
  created() {
    this.load()

  },
  methods: {
    returnCourseName(id){

    },
    filterMethod(query, item) {
      return !query || query == item.type;
    },
    // selectQuestion() {
    //   // 通过备份的原始数据进行筛选
    //   this.paperQuestions = this.paperQuestionsOrigin.filter(v => v.type === this.type)
    // },
    handlePaper(jobId) {
      this.$router.push('/front/studentJob?jobId=' + jobId)
    },
    viewPaper(jobId) {
      this.request.get("/finishUserJob/isSubmitJob/" + jobId + "/" + this.user.id).then(res => {
        if (res.code === '200') {
          this.request.get("/job/view/" + jobId).then(res => {
            this.questiones = res.data
            this.dialogFormVisible2 = true
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    viewJob(jobId) {
      this.request.get("/job/view/" + jobId).then(res => {
        this.questiones = res.data
        this.dialogFormVisible4 = true
      })
    },
    takePaper(jobId, courseId) {
      this.form1 = {type1: 4, type2: 4, type3: 2, jobId: jobId, courseId: courseId}
      this.dialogFormVisible1 = true
    },
    load() {
      this.request.get("/job/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/course").then(res => {
        this.courses = res.data
      })
    },
    generatePaper() {
      this.request.post("/paper/takePaper", this.form1).then(res => {
        if (res.code === '200') {
          this.$message.success("组卷成功")
          this.dialogFormVisible1 = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    generateHandPaper() {
      this.request.post("/job/handPaper", this.form2).then(res => {
        if (res.code === '200') {
          this.$message.success("组卷成功")
          this.dialogFormVisible3 = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    save() {
      this.request.post("/job", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    del(id) {
      this.request.delete("/job/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/job/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
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
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9999/job/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
