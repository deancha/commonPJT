<template>
  <div class="post">
    <h1 class="keyword">{{ searchText }}</h1>

    <div class="table">
      <section class="post-list">
        <div class="search">
          <tr class="search-right" style="height:44px">
            <td style="height:44px">
              <select v-model="key" style="height:44px">
                <option value="null" selected="selected">선택</option>
                <option value="title">제목</option>
                <option value="username">작성자</option>
                <option value="tag">태그</option>
              </select>
            </td>
            <td style="height:44px">
              <input
                style="height:44px"
                type="text"
                v-model="word"
                placeholder="검색어를 입력해주세요."
                @keydown.enter="searchList"
              />
            </td>
            <td style="height:44px">
              <button class="btnn primary searchBtn" @click="searchList" style="height:44px">검색</button>
            </td>
            <div class="write" v-if="$store.state.isAdmin">
              <button class="btnn" @click="temp_post">임시저장</button>
              <button class="btnn" @click="write_post">글쓰기</button>
            </div>
          </tr>
        </div>

        <div class="list" v-if="posts">
          <p class="msg">{{ modifyWord }}</p>
          <section v-for="post in posts" :key="post.post.postid">
            <div class="post">
              <div :class="{ 'post-write': isImg(post) }" @click="mvDetail(post.post.postid)">
                <div class="tags">
                  <span v-for="tag in post.tags" :key="tag" class="tag">#{{ tag }}</span>
                </div>
                <h2 class="title">{{ post.post.title }}</h2>
                <p>{{ post.post.contents }}</p>
              </div>
              <div class="post-detail">
                <span>
                  {{ post.post.createdat.split("T")[0] }}
                  {{ post.post.createdat.split("T")[1].split(":")[0] }}:{{
                  post.post.createdat.split("T")[1].split(":")[1]
                  }}
                </span>
                <span>작성자 : {{ post.post.username }}</span>
                <div class="count">
                  <td v-if="post.post.isLike">
                    <img src="@/assets/img/heartfull.png" />
                  </td>
                  <td v-if="!post.post.isLike">
                    <img src="@/assets/img/heartempty.png" />
                  </td>
                  <td>
                    <span>{{ post.likeCount }}</span>
                  </td>
                  <td>
                    <img src="@/assets/img/comment.png" />
                  </td>
                  <td>
                    <span>{{ post.commentCount }}</span>
                  </td>
                </div>
              </div>
              <div v-if="post.img" class="post-img">
                <img :src="post.img" />
              </div>
            </div>
            <hr class="major" />
          </section>
        </div>
        <infinite-loading v-if="loading" @infinite="infiniteHandler" spinner="waveDots"></infinite-loading>
      </section>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import InfiniteLoading from "vue-infinite-loading";

export default {
  name: "Post",
  components: {
    InfiniteLoading,
  },
  watch: {},
  created() {
    this.getLikeList();
    if (this.$route.query.keyword) {
      this.key = "title";
      this.word = this.$route.query.keyword;
      this.searchList();
    } else if (this.$route.query.tag) {
      this.key = "tag";
      this.word = this.$route.query.tag;
      this.searchList();
    } else this.searchList();
  },

  methods: {
    toast(content, toaster = "b-toaster-top-center", append = false) {
      this.counter++;
      this.$bvToast.toast(content, {
        title: `멋쟁이 상원처럼 알림`,
        toaster: toaster,
        solid: true,
        appendToast: append,
      });
    },
    isImg(data) {
      return data.img;
    },
    mvDetail(data) {
      return this.$router.push("/post/detail/" + data);
    },
    write_post() {
      return this.$router.push("/post/write/");
    },
    temp_post() {
      return this.$router.push("/post/temp");
    },
    searchList() {
      window.scrollTo(0, 0);
      this.modifyWord = "";
      if (this.word == "") {
        this.searchText = "전체글 보기";
        this.loading = false;
        axios.get(process.env.VUE_APP_API_URL + "post/").then(({ data }) => {
          if (data.data == "success") {
            this.totalPosts = data.object;
            this.totalPosts.forEach((post) => {
              post.post.isLike =
                this.likeList.indexOf(post.post.postid) == -1 ? false : true;
              if (post.post.title.length > 50)
                post.post.title = post.post.title.substring(0, 50) + "...";
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
            });
            this.limit = 0;
            this.posts = [];
            this.loading = true;
            if (this.$refs.InfiniteLoading)
              this.$refs.InfiniteLoading.stateChanger.reset();
          } else {
            this.$router.push("/500/리스트를 불러오던 중 에러가 발생했습니다.");
          }
        });
      } else if (this.key == "null") {
        this.toast("검색 범위를 선택해주세요.");
      } else {
        this.loading = false;
        this.searchText = "'" + this.word + "' " + "검색 결과";
        if (this.key != "tag") {
          axios
            .get(process.env.VUE_APP_API_URL + "post/", {
              params: { key: this.key, word: this.word },
            })
            .then(({ data }) => {
              if (data.data == "success") {
                this.totalPosts = data.object;
                this.totalPosts.forEach((post) => {
                  post.post.isLike =
                    this.likeList.indexOf(post.post.postid) == -1
                      ? false
                      : true;
                  if (post.post.title.length > 50)
                    post.post.title = post.post.title.substring(0, 50);
                  post.post.contents = post.post.contents.replace(
                    /(<([^>]+)>)/gi,
                    ""
                  );
                  post.post.contents = post.post.contents.replace(
                    /(&[a-zA-Z]+;)/gi,
                    ""
                  );
                  if (post.post.contents.length > 150)
                    post.post.contents = post.post.contents.substring(0, 150);
                });
                this.posts = [];
                this.limit = 0;
                this.loading = true;
                if (this.$refs.InfiniteLoading)
                  this.$refs.InfiniteLoading.stateChanger.reset();
                // 오탈자 수정한게 같으면 안됨
              } else if (
                data.data == "modify" &&
                data.object.word != this.word
              ) {
                // 오탈자 수정
                this.word = data.object.word;
                this.searchList();
                this.modifyWord =
                  "수정된 검색어 결과 : ' " + data.object.word + " '";
              } else {
                // 진짜 데이터가 없는경우
                this.totalPosts = "";
                this.posts = [];
                this.limit = 0;
                this.loading = true;
                if (this.$refs.InfiniteLoading)
                  this.$refs.InfiniteLoading.stateChanger.reset();
              }
            });
        } else {
          axios
            .get(process.env.VUE_APP_API_URL + "findbyhashtag/", {
              params: { word: this.word },
            })
            .then(({ data }) => {
              if (data.data == "success") {
                this.totalPosts = data.object;
                this.totalPosts.forEach((post) => {
                  post.post.isLike =
                    this.likeList.indexOf(post.post.postid) == -1
                      ? false
                      : true;
                  if (post.post.title.length > 50)
                    post.post.title = post.post.title.substring(0, 50);
                  post.post.contents = post.post.contents.replace(
                    /(<([^>]+)>)/gi,
                    ""
                  );
                  post.post.contents = post.post.contents.replace(
                    /(&[a-zA-Z]+;)/gi,
                    ""
                  );
                  if (post.post.contents.length > 150)
                    post.post.contents = post.post.contents.substring(0, 150);
                });
                this.posts = [];
                this.limit = 0;
                this.loading = true;
                if (this.$refs.InfiniteLoading)
                  this.$refs.InfiniteLoading.stateChanger.reset();
                // 오탈자 수정한게 같으면 안됨
              } else if (
                data.data == "modify" &&
                data.object.word != this.word
              ) {
                // 오탈자 수정
                this.word = data.object.word;
                this.searchList();
                this.modifyWord =
                  "수정된 검색어 결과 : ' " + data.object.word + " '";
              } else {
                // 진짜 데이터가 없는경우
                this.totalPosts = "";
                this.posts = [];
                this.limit = 0;
                this.loading = true;
                if (this.$refs.InfiniteLoading)
                  this.$refs.InfiniteLoading.stateChanger.reset();
              }
            });
        }
      }
    },
    getLikeList() {
      this.likeList = [];
      axios
        .post(process.env.VUE_APP_API_URL + "getUserLikedPosts", {
          useremail: this.$store.state.useremail,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            data.object.forEach((post) => {
              this.likeList.push(post.post.postid);
            });
          }
        });
    },
    infiniteHandler($state) {
      if (this.totalPosts.length > this.limit) {
        axios
          .get(
            process.env.VUE_APP_API_URL +
              "hashtag/" +
              this.totalPosts[this.limit].post.postid
          )
          .then(({ data }) => {
            if (data.data == "success") {
              this.totalPosts[this.limit].tags = [];
              data.object.forEach((tag) => {
                this.totalPosts[this.limit].tags.push(tag.word);
              });
            }
            // 포스트에 추가로 포스트 연결
            this.posts = this.posts.concat(
              this.totalPosts.slice(this.limit, this.limit + 1)
            );
            $state.loaded(); // 계속 받는 상태로 둔다
            this.limit += 1; // 갯수 + 1
          });
      } else {
        $state.complete();
      }
    },
  },
  data: () => {
    return {
      totalPosts: "",
      limit: 0,
      posts: [],
      key: "null",
      word: "",
      searchText: "전체글 보기",
      length: "",
      loading: false,
      likeList: [],
      modifyWord: "",
    };
  },
};
</script>
<style lang="scss" scoped>
a {
  border-bottom: 0;
}
h1 {
  font-size: 30pt;
}
h2 {
  font-size: 1.7em;
}
.keyword {
  text-align: center;
}
.search {
  position: relative;
  height: 90px;
  .search-right {
    position: absolute;
    right: 10px;
  }
  select {
    border-radius: 0.35em 0 0 0.35em;
  }
  input {
    border-radius: 0;
  }
  .searchBtn {
    border-radius: 0 0.35em 0.35em 0;
  }
  .write {
    right: 0;
    position: absolute;
  }
}
.tags {
  margin-bottom: 15px;
  .tag {
    margin-right: 10px;
    padding: 5px;
    border-radius: 2em;
    background-color: #eae5f8;
    color: #566270;
  }
}
.post {
  padding: 20px;
}
.post-detail span {
  margin-right: 20px;
}
.list * {
  word-break: break-all;
}
.list section:hover {
  background-color: #f5f6f7;
}
.count {
  margin-top: 5px;
  text-align: center;
}
.count td {
  vertical-align: middle;
  img {
    height: 20px;
    margin-right: 3px;
  }
}
.major {
  margin-top: 0px;
  margin-bottom: 0px;
}
.msg {
  margin-left: 30px;
  font-size: 13pt;
}
.search-right td {
  padding: 0px;
}
.table td {
  border: 0px;
}
.count td {
  padding: 3px;
}
.write button {
  margin-top: 15px;
  margin-right: 7px;
}
section .post {
  cursor: pointer;
}
</style>
