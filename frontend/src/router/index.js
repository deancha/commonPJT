import Vue from "vue";
import Router from "vue-router";

import constants from "../lib/constants";

// 유저
import Login from "../page/user/Login.vue";
import Profile from "../page/user/Profile.vue";
import Social from "../page/user/Social.vue";

// 포스트
import List from "../page/post/List.vue";
import TempList from "../page/post/TempList.vue";
import Home from "../page/post/Home.vue";
import Edu from "../page/post/Edu.vue";

import PageNotFound from "../components/common/PageNotFound";
import Error from "../components/common/Error";

// 글작성
import Write from "../page/post/Write.vue";
import Modify from "../page/post/Modify.vue";
import TempModify from "../page/post/TempModify.vue";
import Detail from "../page/post/Detail.vue";

// 관리자 페이지
import Charts from "../page/manage/Charts/Chartjs.vue";

// 팀소개 페이지
import Intro from "../page/introduction/TeamIntro.vue";

// 추천 블로그
import Recommend from "../page/recommend/Recommend.vue";

//커리큘럼등록 (임시)
Vue.use(Router);

export default new Router({
  routes: [
    // 로그인/가입

    {
      path: "/user/login",
      name: constants.URL_TYPE.USER.LOGIN,
      component: Login,
    },

    // 포스트
    {
      path: "/post/list",
      name: constants.URL_TYPE.POST.MAIN,
      component: List,
    },
    {
      path: "/",
      name: "Home",
      component: Home,
    },
    //Page Not Found
    {
      path: "*",
      name: constants.PageNotFound,
      component: PageNotFound,
    },
    {
      path: "/user/profile",
      name: "Profile",
      component: Profile,
    },
    {
      path: "/user/social",
      name: "Social",
      component: Social,
    },
    {
      path: "/500/:msg",
      name: Error,
      component: Error,
    },
    {
      path: "/post/Write",
      name: "Write",
      component: Write,
    },
    {
      path: "/post/modify/:id",
      name: "Modify",
      component: Modify,
    },
    {
      path: "/post/temp",
      name: "TempList",
      component: TempList,
    },
    {
      path: "/post/tempModify/:id",
      name: "TempModify",
      component: TempModify,
    },
    {
      path: "/post/detail/:id",
      name: "Detail",
      component: Detail,
    },
    {
      path: "/post/edu",
      name: "Edu",
      component: Edu,
    },
    {
      path: "/manage/charts",
      name: "Charts",
      component: Charts,
    },
    {
      path : "/introduction/intro",
      name : "Intro",
      component : Intro
    },
    {
      path : "/recommend/recommend",
      name : "Recommend",
      component : Recommend
    },
    
  ],
});
