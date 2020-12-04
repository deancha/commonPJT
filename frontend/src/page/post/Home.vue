<template>
  <div style="margin-right:60px; margin-bottom: 20px">
    <!-- section -->

    <section id="banner" style="padding-left:30px;">
      <div class="content">
        <header>
          <h1 style="line-height:160%; font-family:NEXONGothic; font-weight:bold">
            코딩 공부
            <br />어디서부터,
            <br />어떻게 시작할지
            <br />막막하셨나요?
          </h1>
        </header>
        <br />
        <router-link :to="{ name: 'Edu' }">
          <button class="btnn">학습 바로가기</button>
        </router-link>
      </div>

      <span class="image object">
        <img :src="thumnail" style="opacity:80%" />
      </span>
    </section>
    <hr />
    <section class="home-catg" v-if="totalTags">
      <header class="major">
        <h2>Top Hashtag</h2>
      </header>
      <div class="hashtag">
        <button
          v-for="tag in totalTags"
          :key="tag.word"
          @click="$router.push('post/list?tag='+tag.word)"
        >
          <span>#{{ tag.word }}</span>
          <span>{{ tag.cnt }}</span>
        </button>
      </div>
    </section>
    <!-- Section -->
    <section v-if="posts" class="home-catg">
      <header class="major">
        <h2>#Blog</h2>
      </header>
      <div class="posts">
        <article class="post" v-for="post in posts" :key="post.post.postid">
          <div>
            <div v-if="post.tags">
              <span v-for="tag in post.tags" :key="tag">#{{ tag }}</span>
            </div>
            <h2>{{ post.post.title }}</h2>
            <p>{{ post.post.contents }}</p>
          </div>
          <ul class="actions">
            <li>
              <button class="btnn" @click="mvDetail(post.post.postid)">More</button>
            </li>
          </ul>
        </article>
      </div>
    </section>
  </div>
</template>

<script>
// import "@/assets/css/list.css";
import axios from "axios";
export default {
  name: "Post",
  components: {},
  watch: {},
  created() {
    axios.get(process.env.VUE_APP_API_URL + "post/").then(({ data }) => {
      if (data.data == "success") {
        if (data.object.length > 6) this.posts = data.object.slice(0, 6);
        else this.posts = data.object;

        this.posts.forEach((post) => {
          // 해시태그 받아오기
          axios
            .get(process.env.VUE_APP_API_URL + "hashtag/" + post.post.postid)
            .then(({ data }) => {
              if (data.data == "success") {
                post.tags = [];
                data.object.forEach((tag) => {
                  post.tags.push(tag.word);
                });
                post.tags =
                  post.tags.length > 3 ? post.tags.slice(0, 3) : post.tags;
              }
              // 제목이랑 내용 길이 자르기
              post.post.contents = post.post.contents.replace(
                /(<([^>]+)>)/gi,
                ""
              );
              post.post.contents = post.post.contents.replace(
                /(&[a-zA-Z]+;)/gi,
                ""
              );
              if (post.post.contents.length > 150)
                post.post.contents =
                  post.post.contents.substring(0, 150) + "...";
              if (post.post.title.length > 30)
                post.post.title = post.post.title.substring(0, 30) + "...";
            });
        });
        // console.log(this.posts);
      } else {
        // console.log(data);
        this.$router.push("/500");
      }
    });
    this.getTotalTags();
  },
  methods: {
    getTotalTags() {
      axios
        .get(process.env.VUE_APP_API_URL + "gethashtagall")
        .then(({ data }) => {
          if (data.data == "success") {
            this.totalTags = data.object.slice(0, 15);
          }
        });
    },
    isImg(data) {
      return data.img;
    },
    mvDetail(data) {
      this.$router.push("/post/detail/" + data);
    },
  },
  data: () => {
    return {
      thumnail: require("@/assets/img/welcome.jpg"),
      posts: "",
      totalTags: null,
    };
  },
};
</script>
<style lang="scss" scoped>
.hashtag {
  padding: 10px;
  word-break: break-all;
  button {
    border: none;
    background: none;
    margin-bottom: 10px;
    span:first-child {
      margin-right: 0px;
      margin-bottom: 10px;
      padding: 5px;
      border-radius: 2em 0 0 2em;
      background-color: #eae5f8;
      color: #566270;
      border: none;
    }
    span:nth-child(2) {
      margin-right: 5px;
      margin-bottom: 10px;
      padding: 5px;
      padding-right: 7px;
      border-radius: 0 2em 2em 0;
      background-color: #b8abdf;
      color: #fffdfd;
      border: none;
    }
  }
}
.major {
  margin: 70px 0 0 5px;
  > h2 {
    font-family: "NEXONBold";
  }
}
.home-catg {
  border-bottom: 2px solid #f3f3f3;
}
.post {
  padding: 15px;
  h2 {
    font-size: 1.7em;
  }
  > div {
    height: 85%;
    > div {
      margin-bottom: 12px;
      span {
        margin-right: 10px;
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 2em;
        background-color: #eae5f8;
        color: #566270;
      }
    }
  }
}
.image img {
  object-fit: cover;
  max-height: 330px;
}
.posts * {
  word-break: break-all;
}
</style>
