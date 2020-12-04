<template>
  <div id="app">
    <div class="is-preload">
      <div id="wrapper">
        <div id="main">
          <div>
            <header id="header">
              <a href class="logo">
                <img src="@/assets/img/logo.png" height="40px" />
              </a>
            </header>
            <router-view :key="$route.fullPath"/>
          </div>
        </div>
        <Header ref="head"/>
      </div>
    </div>
  </div>
</template>

<script>
import "./assets/css/main.css";
import Header from "./components/common/Header.vue";
import constants from "./lib/constants";

export default {
  name: "App",
  components: {
    Header,
  },
  created() {},
  watch: {},
  methods: {},
  data: function () {
    return {
      constants,
    };
  },
};
</script>
<style scoped>
#main {
  margin-left: 80px;
  margin-right: 80px;
}
@media screen and (max-width: 480px) {
  #main {
    padding: 10px;
    margin: 0;
  }
}
#header {
  padding-top: 30px;
  padding-right: 30px;
  margin-bottom: 20px;
}
</style>

