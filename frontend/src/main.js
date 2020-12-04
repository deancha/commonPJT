import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import { store } from "./store.js";

import { BootstrapVue, BootstrapVueIcons } from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import VueCookie from "vue-cookie";

Vue.use(BootstrapVueIcons);
Vue.use(BootstrapVue);
Vue.use(VueCookie);

Vue.component("modal", {
  template: ["#login", "#signup"],
});

new Vue({
  el: "#app",
  data: {
    showLogin: false,
  },
  router,
  store,
  components: { App },
  template: "<App/>",
});
