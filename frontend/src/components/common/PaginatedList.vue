<template>
  <div>
    <table class="table-wrapper">
      <colgroup>
        <col width="*" />
        <col width="10%" />
        <col width="10%" />
        <col width="20%" />
        <col width="10%" />
      </colgroup>
      <tr>
        <th>포스트 제목</th>
        <th>조회수</th>
        <th>좋아요</th>
        <th>작성일</th>
        <th>수정/삭제</th>
      </tr>
      <tr v-for="p in paginatedData" :key="p.post.postid">
        <td class="item" @click="mvDetail(p.post.postid)">{{ p.post.title }}</td>
        <td>
          <b-icon icon="eye-fill" font-scale="1.5"></b-icon>
          {{ p.viewCount }}
        </td>
        <td>
          <b-icon icon="heart-fill
" font-scale="1.5"></b-icon>
          {{ p.likeCount }}
        </td>
        <td>
          {{ p.post.createdat.split("T")[0] }}
          {{ p.post.createdat.split("T")[1].split(":")[0] }}:{{
          p.post.createdat.split("T")[1].split(":")[1]
          }}
        </td>
        <td>
          <b-icon
            @click="modifyPost(p.post.postid)"
            class="item"
            icon="pencil-square"
            font-scale="1.5"
          ></b-icon>
          <b-icon @click="deletePost(p.post.postid)" class="item" icon="trash" font-scale="1.5"></b-icon>
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

export default {
  name: "paginated-list",
  data() {
    return {
      pageNum: 0,
    };
  },
  props: {
    listArray: {
      type: Array,
      required: true,
    },
    pageSize: {
      type: Number,
      required: false,
      default: 10,
    },
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
    modifyPost(data) {
      // console.log(this.$route.params.id);
      this.$router.push("/post/modify/" + data);
    },
    deletePost(data) {
      if (confirm("정말로 작성하신 글을 삭제하실 건가요?")) {
        axios
          .delete(process.env.VUE_APP_API_URL + "post", {
            params: {
              postid: data,
            },
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.toast("삭제 완료!!");
              this.$router.push("/user/profile");
            } else {
              // console.log(data);
            }
          });
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
  computed: {
    pageCount() {
      if (this.listArray) {
        let listLeng = this.listArray.length,
          listSize = this.pageSize,
          page = Math.floor(listLeng / listSize);
        if (listLeng % listSize > 0) page += 1;

        /*
        아니면 page = Math.floor((listLeng - 1) / listSize) + 1;
        이런식으로 if 문 없이 고칠 수도 있다!
        */
        return page;
      }
    },
    paginatedData() {
      const start = this.pageNum * this.pageSize,
        end = start + this.pageSize;
      return this.listArray.slice(start, end);
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

table td.item:hover {
  cursor: pointer;
  color: #a593e0;
}

.item:hover {
  cursor: pointer;
  color: #a593e0;
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
