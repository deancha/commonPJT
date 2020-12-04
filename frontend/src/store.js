import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    username: "",
    useremail: "",
    profileimg: "",
    accessToken: "",
    social: "",
    isAdmin: false,
  },
  plugins: [createPersistedState()],
  mutations: {
    loginToken(state, payload) {
      state.username = payload.username;
      state.useremail = payload.useremail;
      state.isAdmin = payload.isadmin == "O" ? true : false;
      state.profileimg = payload.profileimg;
      state.social = payload.issocial;
    },
    logout(state) {
      // if (state.social.trim()) {
      // axios
      //   .get(process.env.VUE_APP_API_URL + "naver/deleteToken", {
      //     params: {
      //       accessToken: state.accessToken,
      //     },
      //   })
      //   .then(({ data }) => {
      //     // console.log(data);
      //   });
      // state.accessToken = "";
      // alert("네이버 로그아웃");
      // }
      state.username = "";
      state.useremail = "";
      state.accessToken = "";
      state.isAdmin = false;
      state.profileimg = "";
      state.social = "";
      //this.toast("로그아웃되었습니다.");
      // this.$router.go( { name: 'Home' });
    },
    setToken(state, payload) {
      state.accessToken = payload;
    },
    updatePhoto(state, payload) {
      state.img = payload;
    },
    toast(content, toaster='b-toaster-top-center', append = false) {
        this.counter++
        this.$bvToast.toast(content, {
          title: `멋쟁이 상원처럼 알림`,
          toaster: toaster,
          solid: true,
          appendToast: append
        })
    },
  
  },
});
