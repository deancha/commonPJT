<template>
  <div class="write" id="write">
    <h1>게시글 작성</h1>
    <div class="call-temp">
      <button type="btnn" class="btnn primary" @click="openModal">
        임시저장
      </button>
    </div>
    <MyModal @close="closeModal" v-if="modal">
      <TempList />
    </MyModal>

    <div class="row gtr-uniform">
      <div class="col-12">
        <input
          v-model="title"
          name="title"
          id="title"
          type="text"
          placeholder="제목을 입력하세요"
        />
      </div>
      <div class="col-12 write-contents">
        <editor ref="toastuiEditor" height="500px" />
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
      <button class="btnn" id="TempSaveBtn" @click="tempSave">
        임시저장
      </button>
    </div>
  </div>
</template>

<script>
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";
import MyModal from "@/components/common/MyModal.vue";
import TempList from "@/page/post/TempList.vue";
import axios from "axios";
import VueEmoji from "emoji-vue";

export default {
  name: "Editor",
  components: {
    MyModal,
    TempList,
    editor: Editor,
    VueEmoji,
  },
  data: () => ({
    title: "",
    editorData: "",
    tag: "",
    tags: [],
    modal: false,
  }),
  methods: {
    toast(content, toaster='b-toaster-top-center', append = false) {
        this.counter++
        this.$bvToast.toast(content, {
          title: `멋쟁이 상원처럼 알림`,
          toaster: toaster,
          solid: true,
          appendToast: append
        })
    },
    save() {
      if (!this.title.trim()) {
        this.toast("제목을 입력해주세요!");
        return;
      }
      this.editorData = this.$refs.toastuiEditor.invoke("getMarkdown");
      axios
        .post(process.env.VUE_APP_API_URL + "post", {
          hashtags: this.tags,
          post: {
            title: this.title,
            contents: this.editorData,
            useremail: this.$store.state.useremail,
            username: this.$store.state.username,
          },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("글작성에 성공하였습니다.");
            this.$router.push("/post/list");
          } else {
            this.toast("실패");
            this.$router.push("/500");
          }
        });
    },
    tempSave() {
      axios
        .post(process.env.VUE_APP_API_URL + "posttemp", {
          title: this.title,
          contents: this.editorData,
          useremail: this.$store.state.useremail,
          username: this.$store.state.username,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("글작성에 성공하였습니다.");
            //this.getpostid();
            this.$router.push("/post/temp");
          } else {
            this.toast("실패");
            this.$router.push("/500");
          }
        });
    },
    openModal() {
      this.modal = true;
    },
    closeModal() {
      this.modal = false;
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
.tui-editor * {
  margin: 0;
  padding: 0;
}
.tui-editor-defaultUI-toolbar button {
  box-shadow: none;
  color: grey !important;
}

element.style {
  box-sizing: border-box;
  height: 500px;
  width: 110px;
}
</style>
