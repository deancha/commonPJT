<template>
  <div class="write" id="write">
    <h1>게시글 수정</h1>
    <div class="row gtr-uniform">
      <div class="col-12">
        <input v-model="title" name="title" id="title" type="text" placeholder="제목을 입력하세요" />
      </div>
      <div class="col-12 write-contents">
        <editor v-if="editorData" ref="toastuiEditor" height="500px" :initialValue="editorData" />
      </div>
      <div class="col-12">
        <div class="tags">
          <div class="tag-list">
            <span v-for="tag in tags" :key="tag">
              <a @click="deleteTag(tag)">X</a>
              <span>{{ tag }}</span>
            </span>
            <input
              v-model="tag"
              name="tag"
              id="tag"
              type="text"
              placeholder="해시태그를 입력하세요"
              @keydown.enter="addTag()"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="col-12 write-option">
      <button class="btnn primary" id="SaveBtn" @click="update">저장</button>
    </div>
  </div>
</template>

<script>
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";
import axios from "axios";

export default {
  name: "",
  components: {
    editor: Editor,
  },
  created() {
    var postid = this.$route.params.id;
    axios
      .post(process.env.VUE_APP_API_URL + "selectpost", {
        postid: postid,
        useremail: this.$store.state.useremail,
      })
      .then(({ data }) => {
        if (data.data == "success") {
          this.title = data.object.title;
          this.editorData = data.object.contents;
          this.createdat = data.object.createdat;
        } else {
          // console.log(data);
        }
      });
    axios
      .get(process.env.VUE_APP_API_URL + "hashtag/" + postid)
      .then(({ data }) => {
        if (data.data == "success") {
          data.object.forEach((element) => {
            this.tags.push(element.word);
            this.baseTags.push(element.word);
          });
        } else {
          // console.log(data);
        }
      });
  },
  data: () => ({
    title: "",
    createdat: "",
    editorData: "",
    tag: "",
    tags: [],
    deleteTags: [],
    baseTags: [],
    modal: false,
  }),
  methods: {
    update() {
      this.editorData = this.$refs.toastuiEditor.invoke("getMarkdown");
      // console.log(this.editorData);
      axios
        .put(process.env.VUE_APP_API_URL + "post", {
          title: this.title,
          contents: this.editorData,
          username: this.$store.state.username,
          useremail: this.$store.state.useremail,
          postid: this.$route.params.id,
          createdat: this.createdat,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            // 해시태그 추가 수정 or 삭제
            this.tags.forEach((tag) => {
              if (this.baseTags.indexOf(tag) == -1) {
                axios.post(process.env.VUE_APP_API_URL + "hashtag", {
                  postid: this.$route.params.id,
                  word: tag,
                });
              }
            });
            this.deleteTags.forEach((tag) => {
              if (this.baseTags.indexOf(tag) != -1) {
                axios.delete(process.env.VUE_APP_API_URL + "hashtag", {
                  params: {
                    postid: this.$route.params.id,
                    word: tag,
                  },
                });
              }
            });
            this.$router.push("/post/list");
          } else {
            // console.log(data);
          }
        });
    },
    addTag() {
      if (this.tag.trim()) {
        if (this.tags.indexOf(this.tag) != -1)
          this.tags.splice(this.tags.indexOf(this.tag), 1);
        this.tags.push(this.tag);
        this.tag = "";
      }
    },
    deleteTag(data) {
      this.tags.splice(this.tags.indexOf(data), 1);
      this.deleteTags.push(data);
    },
  },
};
</script>
<style lang="scss" scoped>
h1 {
  font-size: 25pt;
  text-align: center;
  margin-bottom: 40px;
}
.call-temp {
  width: 100%;
  text-align: right;
  padding: 20px;
}

.write-option {
  text-align: center;
  button {
    margin: 20px;
  }
}
.gtr-uniform {
  width: 90%;
  margin: 0 auto;
}
.ck-button {
  box-shadow: none;
}
.tags {
  height: fit-content;
  background: #ffffff;
  border-radius: 0.375em;
  border: solid 1px rgba(210, 215, 217, 0.75);
  color: inherit;
  outline: 0;
  padding: 0 1em;
  text-decoration: none;
  width: 100%;
  .tag-list {
    > span {
      white-space: nowrap;
      height: 3em;
      text-align: center;
      border: 1px solid #e0e3da;
      border-radius: 2em;
      box-shadow: none;
      background-color: #e0e3da;
      padding: 10px;
      margin: 5px;
      display: inline-block;
      a {
        color: #566270;
        margin-right: 5px;
        border: 0;
      }
    }
    > input {
      display: inline-block;
      width: auto;
      min-width: 100px;
    }
  }

  input,
  input:hover:focus {
    overflow: hidden;
    background-color: none;
    border: 0;
    box-shadow: none;
  }
}
</style>
