<template>
  <div class="write" id="write">
    <h1>임시 게시글 수정</h1>
    <div class="row gtr-uniform">
      <div class="col-12">
        <input v-model="title" name="title" id="title" type="text" placeholder="제목을 입력하세요" />
      </div>
      <div class="col-12 write-contents">
        <ckeditor :editor="editor" v-model="editorData" :config="editorConfig" />
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
      <button class="btnn primary" id="SaveBtn" @click="save">저장</button>
      <button class="btnn" id="TempSaveBtn" @click="update">임시저장</button>
    </div>
  </div>
</template>

<script>
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import CKEditor from "@ckeditor/ckeditor5-vue";
import axios from "axios";

export default {
  name: "CKEditor",
  components: {
    ckeditor: CKEditor.component,
  },
  created() {
    var postid = this.$route.params.id;
    axios
      .get(process.env.VUE_APP_API_URL + "posttemp/topost/" + postid)
      .then(({ data }) => {
        if (data.data == "success") {
          this.title = data.object.title;
          this.username = data.object.username;
          this.editorData = data.object.contents;
        } else {
          // console.log(data);
        }
      });
  },
  data: () => ({
    title: "",
    username: "",
    editor: ClassicEditor,
    editorData: "",
    editorConfig: {
      height: "500px",
      language: "ko",
    },
    tag: "",
    tags: [],
  }),
  methods: {
    update() {
      axios
        .put(process.env.VUE_APP_API_URL + "posttemp", {
          title: this.title,
          contents: this.editorData,
          username: this.$store.state.username,
          useremail: this.$store.state.useremail,
          postid: this.$route.params.id,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.$router.push("/post/temp");
          } else {
            // console.log(data);
          }
        });
    },
    save() {
      axios
        .post(process.env.VUE_APP_API_URL + "post", {
          hashtags: [],
          post: {
            title: this.title,
            contents: this.editorData,
            useremail: this.$store.state.useremail,
            username: this.$store.state.username,
          },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            axios
              .delete(process.env.VUE_APP_API_URL + "posttemp", {
                params: { postid: this.$route.params.id },
              })
              .then(({ data }) => {
                if (data.data == "success") {
                  this.$router.push("/post/list");
                } else {
                  // console.log(data);
                }
              });
          } else {
            // console.log(data);
          }
        });
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
