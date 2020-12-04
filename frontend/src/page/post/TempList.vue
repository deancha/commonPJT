<template>
  <div class="post">
    <h1>임시 저장 게시글</h1>
    <div class="list">
      <section class="row" v-for="temp in tempPosts" :key="temp.postid">
        <div class="col-10" @click="mvModify(temp.postid)">
          <h2 class="title">{{ temp.title }}</h2>
          <div class="post-detail">
            <span>{{ temp.createdat }}</span>
          </div>
        </div>
        <div class="col-1">
          <button type="btnn" class="btnn primary" @click="deletePost(temp.postid)">삭제</button>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Post",
  components: {},
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    mvModify(data) {
      this.$router.push("/post/tempModify/" + data);
    },
    deletePost(data) {
      axios
        .delete(process.env.VUE_APP_API_URL + "posttemp", {
          params: { postid: data },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.getList();
          } else {
            // console.log(data);
          }
        });
    },
    getList() {
      axios
        .get(
          process.env.VUE_APP_API_URL +
            "posttemp/" +
            this.$store.state.useremail
        )
        .then(({ data }) => {
          if (data.data == "success") {
            this.tempPosts = data.object;
            this.tempPosts.forEach((post) => {
              if (post.title.length > 40)
                post.title = post.title.substring(0, 40) + "...";
            });
          } else {
            // console.log(data);
          }
        });
    },
  },
  data: () => {
    return {
      tempPosts: "",
    };
  },
};
</script>
<style scoped>
h1 {
  font-size: 30pt;
  text-align: center;
}
.list {
  width: 80%;
  margin: 0 auto;
}
.list * {
  word-break: break-all;
}
.list section {
  display: table;
  padding: 15px;
  border-bottom: 2px solid #f3f3f3;
}
.list section div {
  display: table-cell;
}
.list section:hover {
  background-color: #e0e3da4d;
}
</style>
