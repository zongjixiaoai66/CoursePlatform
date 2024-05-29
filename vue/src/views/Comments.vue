<template>
  <div style="margin: 10px 0 50px 0">

    <el-input type="textarea" :rows="5" v-model="form.content"></el-input>
    <div style="text-align: right; margin: 10px 0">
      <el-button type="primary" @click="save">留言</el-button>
    </div>

    <div style="padding: 10px; border-top: 1px solid #ccc">
      <div v-for="item in tableData" :key="item.id">
        <div style="margin: 10px 0; font-size: 18px">{{ item.content }} <span
            style="margin-left: 20px; font-size: 12px; color: #666">{{ item.user }} <span
            style="margin-left: 10px">{{ item.createTime }}</span></span></div>

        <div style="margin: 10px 0">
          <el-button type="text" @click="replys(item.id, item.user)">回复</el-button>
          <el-button style="color: red" type="text" @click="del(item.id)" v-if="user.id === item.userid">删除</el-button>
        </div>
        <div style="margin: 10px 30px; background-color: aliceblue; padding: 10px " v-if="item.children.length">
          <div v-for="sub in item.children" :key="sub.id">
            <div style="margin: 10px 0; font-size: 18px"> <span style="color: #666;">回复 <b style="color: orange; margin-right: 20px">{{ sub.replyUser }}:</b></span>{{ sub.content }} <span
                style="margin-left: 20px; font-size: 12px; color: #666">
              {{ sub.user }} <span style="margin-left: 10px">{{ sub.createTime }}</span></span></div>
            <div style="margin: 10px 0">
              <el-button type="text" @click="replys(item.id,  sub.user)">回复</el-button>
              <el-button style="color: red" type="text" @click="del(sub.id)" v-if="user.id === sub.userid">删除</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="reply" :rules="rules" ref="ruleForm">
        <el-form-item prop="reply" label="内容">
          <el-input type="textarea" v-model="reply.content" autocomplete="off"></el-input>
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
  name: "Comment",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        content: [
          { required: true, message: '请输入留言', trigger: 'blur'}
        ],
      },
      reply: {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    replys(pid, username) {
      this.reply = {}
      this.reply.pid = pid
      this.reply.replyUser = username
      this.dialogFormVisible = true
    },
    load() {
      this.request.get("/comment/tree").then(res => {
        this.tableData = res.data
      })
    },
    save() {
      if (!this.form.content && !this.reply.content) {
        this.$message.error("请填写内容")
        return
      }
      if (!this.form.content) {
        this.form.content = this.reply.content
        this.form.pid = this.reply.pid
        this.form.replyUser = this.reply.replyUser
      }

      this.form.name = this.user.nickname + '的留言'
      this.form.userid = this.user.id
      this.form.user = this.user.nickname
      this.request.post("/comment", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("留言成功")
          this.form.content = ''
          this.load()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    del(id) {
      this.request.delete("/comment/" + id).then(res => {
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
      this.request.post("/comment/del/batch", ids).then(res => {
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
      window.open("http://localhost:9999/comment/export")
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
  background: #eee !important;
}
</style>
