<template>
  <div>
    <table class="table-wrapper">
      <colgroup>
        <col width="*" />
        <col width="20%" />
        <col width="8%" />
      </colgroup>
      <tr>
        <th>댓글 내용</th>
        <th>작성일</th>
        <th>삭제</th>
      </tr>
      <tr v-for="c in pageArray" :key="c.comment.commentsid">
        <td @click="mvDetail(c.comment.postid)">{{ c.comment.contents }}</td>
        <td>
          {{ c.comment.createdat.split("T")[0] }}
          {{ c.comment.createdat.split("T")[1].split(":")[0] }}:{{
          c.comment.createdat.split("T")[1].split(":")[1]}}
        </td>

        <td>
          <b-icon icon="trash" font-scale="1.5" @click="deleteComment(c.comment.commentsid)"></b-icon>
        </td>
      </tr>
    </table>
    <div class="btn-cover">
      <button :disabled="pageNum === 0" @click="prevPage" class="btnn page-btn">이전</button>
      <span class="page-count">{{ pageNum + 1 }} / {{ pageCount }} 페이지</span>
      <button :disabled="pageNum >= pageCount - 1" @click="nextPage" class="btnn page-btn">다음</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Login from "@/page/user/Login.vue";

export default {
  name: "paginated-comment-list",
  data() {
    return {
      pageArray: [],
      username: "",
      pageNum: 0,
    };
  },
  props: {
    pageSize: {
      type: Number,
      required: false,
      default: 10,
    },
  },
  components: { Login },
  methods: {
    getMyPosts() {
      axios
        .post(process.env.VUE_APP_API_URL + "getUserCommentedPosts", {
          useremail: this.$store.state.useremail,
        })
        .then(({ data }) => {
          this.pageArray = data.object;
        })
        .catch((err) => {
          // console.log(err);
        });
    },
    deleteComment(data) {
      if (confirm("정말로 작성하신 댓글을 삭제하실 건가요?")) {
        axios.delete(process.env.VUE_APP_API_URL + "comments", {
          params: {
            commentsid: data,
          },
        });
        // this.$router.push("/user/profile"); 이거 해도 안됨.. 댓글삭제 전 화면 그대로
      }
    },

    mvDetail(data) {
      return this.$router.push("/post/detail/" + data);
    },
    nextPage() {
      this.pageNum += 1;
    },
    prevPage() {
      this.pageNum -= 1;
    },
  },

  created() {
    this.getMyPosts();
  },

  computed: {
    pageCount() {
      if (this.pageArray) {
        let listLeng = this.pageArray.length,
          listSize = this.pageSize,
          page = Math.floor(listLeng / listSize);
        if (listLeng % listSize > 0) page += 1;
        return page;
      }

      /*
      아니면 page = Math.floor((listLeng - 1) / listSize) + 1;
      이런식으로 if 문 없이 고칠 수도 있다!
      */
    },
    paginatedData() {
      const start = this.pageNum * this.pageSize,
        end = start + this.pageSize;
      return this.pageArray.slice(start, end);
    },
  },
};
</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
}
table th {
  font-size: 0.9rem;
  font-weight: 600;
  padding: 0 0.75em 0.75em 0.75em;
  vertical-align: middle;
  color: #3d4449;
  text-align: left;
}
table tr {
  height: 2rem;
  text-align: center;
  padding: 1rem 0;
  border-bottom: 1px solid grey;
}
table tr:first-of-type {
  border-bottom: 2px solid grey;
}

table td {
  vertical-align: baseline;
}

table tr td {
  padding: 1rem 0;
  font-size: 1.1rem;
  color: #7f888f;
}
button {
  text-align: center;
  justify-content: center;
}

.table-wrapper {
  word-break: break-all;
  -webkit-overflow-scrolling: touch;
  overflow-x: auto;
  word-break: break-all;
  font-size: 1em;
  padding-left: 7px;
  text-align: left;
  margin-top: 20px;
}

.btn-cover {
  margin-top: 1.5rem;
  text-align: center;
  cursor: default;
  vertical-align: middle;
}
.btn-cover .page-btn {
  /* width: 5rem; */
  height: 2rem;
  letter-spacing: 0.5px;
  text-align: left;
}
.btn-cover .page-count {
  padding: 0 1rem;
}
</style>