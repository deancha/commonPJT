<template>
  <div>
    <h3>{{ $store.state.username }}님이 작성한 포스트 입니다</h3>
    <paginated-list :list-array="pageArray" />
    <!-- 
    <h3 :v-if="title==='댓글'">{{ $store.state.username }}님이 좋아요를 누른 포스트 입니다</h3>
    <paginated-like-list :list-array="pageLikeArray" />-->
  </div>
</template>

<script>
import axios from "axios";
import PaginatedList from "./PaginatedList";
import Login from "@/page/user/Login.vue";

export default {
  name: "TablePagination",
  components: {
    PaginatedList,
    Login,
  },
  data() {
    return {
      pageArray: [],
      username: "",
    };
  },
  methods: {
    getMyPosts() {
      axios
        .get(process.env.VUE_APP_API_URL + "post", {
          params: {
            key: "useremail",
            word: this.$store.state.useremail,
          },
        })
        .then(({ data }) => {
          // console.log("data", data);
          this.pageArray = data.object;
        })
        .catch((err) => {
          // console.log(err);
        });
    },
  },
  created() {
    this.getMyPosts();
  },
};
</script>

<style scoped>
h3 {
  color: #454545;
  text-align: center;
  align-items: center;
  justify-content: center;
  display: flex;
  margin-bottom: 30px;
}
</style>
