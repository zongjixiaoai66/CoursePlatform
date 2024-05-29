<template>
  <div style="margin-bottom: 100px">
    <div style="margin: 20px 0">
      <span style="font-size: 20px; color: #333">作业名称{{ job.name }}</span>
    </div>

   <div style="margin: 10px 0">
     <el-card>
       <div v-for="(item, index) in questiones" :key="item.id"  style="margin: 20px 0">
         <div style="margin: 10px 0; font-size: 20px"><span>{{ index+1 }}.</span> {{ item.name }}
           <span style="font-size: 14px" v-if="item.type === 1">（选择题）</span>
           <span style="font-size: 14px"  v-if="item.type === 2">（判断题）</span>
           <span style="font-size: 14px"  v-if="item.type === 3">（问答题）</span>
         </div>
         <div v-if="item.type === 1" style="margin: 10px">
           <span style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="A">A. {{ item.a }}</el-radio></span>
           <span  style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="B">B. {{ item.b }}</el-radio></span>
           <span  style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="C">C. {{ item.c }}</el-radio></span>
           <span  style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="D">D. {{ item.d }}</el-radio></span>
         </div>
         <div v-if="item.type === 2" style="margin: 10px">
           <span style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="是">是</el-radio></span>
           <span  style="margin-right: 20px"><el-radio v-model="item['studentAnswer']" label="否">否</el-radio></span>
         </div>
         <div v-if="item.type === 3" style="margin: 10px">
           <el-input type="textarea" v-model="item['studentAnswer']"></el-input>
         </div>
<!--         <div style="margin: 10px">-->
<!--           答案： {{ item.answer }}-->
<!--         </div>-->
<!--         <div style="margin: 10px">-->
<!--           解析： {{ item.detail }}-->
<!--         </div>-->
       </div>
     </el-card>

     <div style="margin: 20px; text-align: center">
       <el-button size="medium" type="primary" @click="submitJob">提 交</el-button>
       <el-button size="medium" type="warning" @click="$router.push('/front/frontJob')">取 消</el-button>
     </div>
   </div>
  </div>
</template>

<script>
export default {
  name: "StudentJob",
  data() {
    return {
      questiones: [],
      jobId: this.$route.query.jobId,
      job: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.request.get("/job/" + this.jobId).then(res => {
      this.job = res.data
    })

    this.request.get("/jobQuestion/findByJobId/" + this.jobId).then(res => {  // 根据作业id查询题目信息
          this.questiones = res.data
    })
  },
  methods: {
    submitJob() {
      this.request.get("/finishUserJob/submitJob/" +  this.jobId + "/" + this.user.id).then(res => {
        if (res.code === '200') {
          this.$message.success("完成作业成功！")
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>