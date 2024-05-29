<template>
  <div>
<!--    头部-->
    <div style="display: flex; height: 100px; line-height: 60px; border-bottom: 1px solid #eee">
      <div style="width: 300px; display: flex; padding-left: 30px">
        <div style="width: 60px">
          <img src="../../assets/logo1.png" alt="" style="width: 50px; position: relative; top: 8px; right: 0">
        </div>
        <div style="flex: 1">欢迎来到课件管理平台</div>
      </div>
      <div style="flex: 1;margin-top: 10px; margin-bottom: 10px">
        <el-card style="height: 80px; width: 800px;text-align: center;display: flex; justify-content: center">
          <template>
            <el-menu :default-active="'1'" class="el-menu-demo" style="display: flex;justify-content: center;line-height: 30px" mode="horizontal" router>
              <el-menu-item index="/front/home">
                首页
              </el-menu-item>
              <el-menu-item index="/front/studentComments">留言</el-menu-item>
              <el-menu-item index="/front/studentNotice">课件信息</el-menu-item>
              <el-menu-item index="/front/studentLog">学习日志</el-menu-item>
              <el-menu-item index="/front/frontJob">我的作业</el-menu-item>
              <el-menu-item index="/front/teacherEvaluate">教师教学评价</el-menu-item>
              <el-menu-item index="/front/frontExamAppeal">考试成绩申诉</el-menu-item>
            </el-menu>
          </template>
        </el-card>
      </div style="flex: 1">
      <div style="width: 200px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width: 150vh; margin: 0 auto;height:80vh;">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  },
  methods: {
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style>
.el-menu--horizontal>.el-menu-item {
  float: left;
  height: 0px;
  line-height: 60px;
  margin: 0;
  text-align: center;
  border-bottom: 2px solid transparent;
  color: #909399;
}
.el-card {
  width: auto;
  border: none;
  margin-bottom: 20px;
  box-shadow: none;
}
.item{
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer
}
.item a {
  color: 	#1E90FF;
}
.item:hover{
  background-color: 	LightPink;
}
</style>
