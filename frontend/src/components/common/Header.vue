<template>
  <div>
    <div id="sidebar" :class="{ inactive: isSide }" style="height:100%">
      <div class="inner">
        <div class="user" style="border:0; margin:0; padding: 0; ">
          <div class="visitor per day">Today &nbsp;&nbsp; {{this.visitors}}명</div>
          <div v-if="!$store.state.username" style="position:relative; height: 60px">
            <a
              @click="openModal"
              class="link dohyeon"
              style="border:0px; position:absolute; right:0px"
            >로그인</a>
          </div>
          <MyModal @close="closeModal" v-if="loginModal">
            <Login @close="closeModal" />
          </MyModal>
          <div v-if="$store.state.username">
            <div style="position:relative; height: 20px">
              <a v-if="$store.state.username" style="border:0px; position:absolute; right:70px">
                <router-link :to="{ name: 'Profile' }" class="link dohyeon">내정보</router-link>
              </a>
              <a
                href="#"
                type="button"
                @click="logout"
                class="link dohyeon"
                style="border:0px; position:absolute; right:0px"
              >로그아웃</a>
            </div>
            <br />

            <div class="userinfo" style="display:relative; height:120px">
              <!-- <b-avatar size="lg" :src="Sangwon"></b-avatar> -->
              <b-avatar size="lg" :src="$store.state.profileimg"></b-avatar>
              <div
                style="border: 0px; padding-bottom:10px; margin-bottom: 30px; position:absolute; left:100px; top:100px"
              >
                <p>{{ $store.state.username }} 님 환영합니다!</p>
              </div>
            </div>
          </div>
        </div>
        <!-- Search -->
        <section id="search" class="alt" style="position:relative">
          <img src="@/assets/img/돋보기.png" class="search-icon" height="20px" />
          <input type="text" v-model="keyword" @keydown.enter="search()" placeholder="검색어를 입력하세요." />
        </section>

        <!-- Menu -->
        <nav id="menu">
          <header class="major">
            <h2 class="dohyeon">Menu</h2>
          </header>
          <ul>
            <li>
              <router-link
                class="link dohyeon"
                v-bind:to="{ name: constants.URL_TYPE.POST.MAIN }"
              >블로그</router-link>
            </li>
            <li>
              <router-link :to="{ name: 'Edu' }" class="link dohyeon">교육</router-link>
            </li>

            <li>
              <router-link :to="{ name: 'Recommend' }" class="link dohyeon">추천 블로그</router-link>
            </li>

            <!-- <li v-if="$store.state.isAdmin"> -->
            <li>
              <router-link :to="{ name: 'Charts' }" class="link dohyeon">통계</router-link>
            </li>
          </ul>
        </nav>

        <!-- Section -->
        <div>
          <header class="major">
            <h2 class="dohyeon">Hot Post</h2>
          </header>
          <b-card
            class="hotpost"
            v-for="post in hotPosts"
            :key="post.post.postid"
            @click="mvDetailPage(post.post.postid)"
          >
            <strong>{{ post.post.title }}</strong>
            <br />
            <hr class="major" />
            <b-card-text>조회수 : {{ post.viewCount }}</b-card-text>
          </b-card>
        </div>

        <!-- 푸터 부분  -->
        <section>
          <router-link class="link" :to="{ name: 'Intro' }">
            <h2 class="dohyeon">About us</h2>
          </router-link>

          <ul class="contact">
            <a href="https://edu.ssafy.com/" target="_blank" class="dohyeon">
              <li class="icon solid fa-home">EDU SSAFY</li>
            </a>
            <br />
            <a href="https://project.ssafy.com/" target="_blank" class="dohyeon">
              <li class="icon solid fa-home">SSAFY GIT</li>
            </a>
          </ul>
        </section>

        <!-- Footer -->
      </div>
      <a class="toggle" @click="openSide"></a>
    </div>
  </div>
</template>

<script>
import constants from "../../lib/constants";
import MyModal from "@/components/common/MyModal.vue";
import Login from "@/page/user/Login.vue";
import axios from "axios";
import firebase from "firebase";

export default {
  name: "Header",
  components: { MyModal, Login },
  props: ["isHeader"],
  data: () => {
    return {
      constants,
      keyword: "",
      username: "",
      loginModal: false,
      message: "",
      isOpenCategory: false,
      isSubmenu: false,
      isSide: false,
      opener: "opener",
      hotPosts: "",
      // imagePreview: "",
    };
  },
  computed: {},
  watch: {},

  created() {
    this.cookiecheck();
    axios.get(process.env.VUE_APP_API_URL + "getHotpost").then(({ data }) => {
      if (data.data == "success") {
        if (data.object.length > 3) {
          this.hotPosts = data.object.slice(0, 3);
        } else {
          this.hotPosts = data.object;
        }

        this.hotPosts.forEach((post) => {
          if (post.post.title.length > 20)
            post.post.title = post.post.title.substring(0, 20) + "...";
          if (post.post.contents.length > 40)
            post.post.contents = post.post.contents.substring(0, 40) + "...";
        });
      }
    });

    // this.imagePreview = this.$store.state.profileimg;
  },

  mounted() {
    this.onResize();
    window.addEventListener("resize", this.onResize);
  },
  methods: {
    cookiecheck() {
      //쿠키가 없으면 쿠키를 만들어주고 (만료시간 :12시간), axios통신으로 방문자수를 증가시켜준다.
      if (this.$cookie.get("visit") == null) {
        this.$cookie.set("visit", "cookie expire", { expires: "12h" });
        axios.put(process.env.VUE_APP_API_URL + "visitor").then(({ data }) => {
          this.visitors = data.object;
        });
      } else {
        //쿠키가 있으면 방문자 수만 반환받는다.
        axios.get(process.env.VUE_APP_API_URL + "visitor").then(({ data }) => {
          this.visitors = data.object;
        });
      }
    },
    mvDetailPage(data) {
      this.$router.push("/post/detail/" + data);
    },
    onResize() {
      this.isSide = window.innerWidth < 1280;
    },
    toast(content, toaster = "b-toaster-top-center", append = false) {
      this.counter++;
      this.$bvToast.toast(content, {
        title: `멋쟁이 상원처럼 알림`,
        toaster: toaster,
        solid: true,
        appendToast: append,
      });
    },
    logout() {
      this.$store.commit("logout");
      this.toast("로그아웃 되었습니다.");
      this.$router.push("/");
    },

    openModal() {
      this.loginModal = true;
    },
    closeModal() {
      this.loginModal = false;
    },
    search() {
      this.$router.push("/post/list?keyword=" + this.keyword);
      this.keyword = "";
    },
    category() {
      this.isOpenCategory = !this.isOpenCategory;
    },
    openSide() {
      this.isSide = !this.isSide;
    },
  },
};
</script>

<style scoped>
a,
.hotpost {
  cursor: pointer;
}
.userinfo {
  font-size: 12pt;
}

.major2 {
  height: 2px;
  background-color: #a593e0;
}

.link {
  display: block;
  border: 0px;
  padding: 5px;
  margin-bottom: 10px;
}
</style>
