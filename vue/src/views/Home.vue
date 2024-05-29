<template>
  <div style="color: #666;font-size: 14px;" id="building">
    <div style="padding-bottom: 20px">
      <b>您好！{{ user.nickname }}</b>
    </div>
    欢迎使用本系统


  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>

<style>
#building {
  background: url("../assets/admin.jpg");
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}
</style>
