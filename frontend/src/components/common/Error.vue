<template>
  <div id="oopss">
    <div id="error-text" >
      <img src="https://cdn.rawgit.com/ahmedhosna95/upload/1731955f/sad404.svg" alt="500" />
      <h3>
        <strong>오류가 발생했습니다</strong>
        <p>{{ msg }}</p>
      </h3>
    </div>
  </div>
</template>

<script>
// import "../../assets/css/pagenotfound.css";
export default {
  name: "Error",
  created() {
    this.msg = this.$route.params.msg;
  },
  data() {
    msg: "";
  },
};
</script>  

<style>

#error-text{
  text-align: center;
}
</style>