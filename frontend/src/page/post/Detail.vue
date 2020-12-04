<template>
  <div class="detail" align="left">
    <div class="detail-view">
      <div id="pdfsector" v-if="title">
        <h2 class="detail-title" id="title">{{ title }}</h2>
        <div class="detail-info">
          {{ username }}
          <span class="detail-btn">
            {{ createdat.split("T")[0] }}
            {{ createdat.split("T")[1].split(":")[0] }}:{{
            createdat.split("T")[1].split(":")[1]
            }}
            <span
              v-if="useremail == $store.state.useremail"
            >
              <a id="modifyPost" v-on:click="modifyPost" style="color:#566270">수정</a> &nbsp;
              <a id="deletePost" v-on:click="deletePost" style="color:#F16B6F">삭제</a>
            </span>
          </span>
        </div>
        <div>
          <img v-if="!liked" :src="heartempty" @click="like" height="25px" style="cursor:pointer" />
          <img v-if="liked" :src="heartfull" @click="like" height="25px" style="cursor:pointer" />
          <span style="margin-left:5px;">{{ likenumber }}</span>
        </div>
        <div class="detail-contents">
          <Viewer v-if="contents" :initialValue="contents" />
        </div>
      </div>
      <div class="detail-footer">
        <a @click="shareOpen">
          <img src="@/assets/img/linkblack.png" height="20px" align="middle" />공유
        </a>
        <div class="share-icon" style="display : none" @mouseleave="shareClose">
          <img src="@/assets/img/link.png" @click="share('link')" />
          <img src="@/assets/img/twitter.png" @click="share('twit')" />
          <!-- <img src="@/assets/img/kakaotalk.png" @click="share('kakaotalk')" /> -->
          <img src="@/assets/img/kakaostory.png" @click="share('kakaostory')" />
          <img src="@/assets/img/facebook.png" @click="share('face')" />
        </div>
      </div>
    </div>

    <table class="co">
      <colgroup>
        <col width="50px" />
        <col width="130px" />
        <col width="*" />
        <col width="*" />
        <col width="10%" />
      </colgroup>
      <tr
        style="height:50px;"
        class="comment"
        v-for="comment in comments"
        :key="comment.comment.commentsid"
      >
        <td>
          <b-avatar :src="comment.profileimg"></b-avatar>
        </td>
        <td class="comment-username">{{ comment.comment.username }}</td>
        <td
          class="comment-contents"
          :id="'td' + comment.comment.commentsid"
          style="display:"
        >{{ comment.comment.contents }}</td>
        <td>
          <input class="inputCom" type="hidden" v-model="nComment" :id="comment.comment.commentsid" />
        </td>
        <td class="comment-btn" v-if="comment.comment.useremail == $store.state.useremail">
          <a
            class="openCom"
            :id="'open' + comment.comment.commentsid"
            @click="open(comment.comment)"
            style="color:#566270; display: "
          >수정</a>
          &nbsp;
          <a
            class="submit"
            :id="'close' + comment.comment.commentsid"
            @click="updateComment(comment.comment)"
            style="color:#566270; display: none"
          >등록</a>
          &nbsp;
          <a
            @click="deleteComment(comment.comment.commentsid)"
            style="color:#F16B6F"
          >삭제</a>
        </td>
      </tr>
    </table>
    <br />
    <section>
      <div v-if="$store.state.useremail">
        <h4 style="padding-left: 10px">댓글 쓰기</h4>
        <b-row class="write-comment">
          <VueEmoji
            style="vertical-align:middle; display:inline-block; width: 90%"
            @input="onInput"
            :value="writeCom"
          />
          <button
            class="btnn"
            @click="writeComment"
            style="height:35px; border-radius: 0 0.5em 0.5em 0"
          >등록</button>
        </b-row>
      </div>
      <br />
      <br />
      <br />
    </section>
      <div v-if="relativePost.length > 0" class="postbox">
        <h5 class="postboxtitle">현재 포스트와 관련된 포스트입니다</h5>
        <div>
          <table class="alt">
            <thead>
              <tr>
                <th>Tag</th>
                <th>Title</th>
                <th>Writer</th>
              </tr>
            </thead>
            <tbody class="alt">
              <tr v-for="post in relativePost" :key="post.postid">
                <td class="relative-tag" @click="searchTag(post.tag)">#{{ post.tag }}</td>
                <td class="relative-title" @click="mvDetail(post.postid)">{{ post.title }}</td>
                <td>{{ post.username }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-if="$store.state.useremail && recentViews.length > 0" class="postbox">
        <h5 class="postboxtitle">최근에 본 포스트입니다</h5>
        <div>
          <table class="alt">
            <thead>
              <tr>
                <th>Title</th>
                 <th>Date</th>
                <th>Writer</th>
              </tr>
            </thead>
            <tbody class="alt">
              <tr v-for="recent in recentViews" :key="recent.postid">
                <td class="relative-title" @click="mvDetail(recent.postid)">{{ recent.title }} {{i}} </td>
                <td>{{ recent.createdat.slice(0,10) }}</td>
                <td>{{ recent.username }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    <div style="text-align:right;">
      <button class="btnn primary" @click="makePDF" style="margin-bottom : 30px">PDF저장</button>
      &nbsp;
      <button class="btnn primary" @click="mvList" style="margin-bottom : 30px">목록</button>
    </div>
  </div>
</template>

<script>
import "@toast-ui/editor/dist/toastui-editor.css";
import { Viewer } from "@toast-ui/vue-editor";
import axios from "axios";
import VueEmoji from "emoji-vue";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

import "@/assets/css/detail.css";

export default {
  name: "detail",
  components: {
    Viewer,
    VueEmoji,
  },
  data: () => {
    return {
      comments: "",
      writeCom: "",
      nComment: "",
      beforeCom: "",
      title: "",
      username: "",
      useremail: "",
      contents: "",
      createdat: "",
      liked: false,
      likenumber: "",
      heartempty: require("@/assets/img/heartempty.png"),
      heartfull: require("@/assets/img/heartfull.png"),
      relativePost: [],
      relativePostid: [],
      recentViews: [],
      recommendViews: [],
      propTitle: "mypdf",
    };
  },

  watch: {},
  created() {
    this.getPost();
    this.getComment();
    this.getRelativePost();
    this.getlikenumber();
    this.getLike();
    this.getRecentView();
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
    searchTag(data) {
      this.$router.push("/post/list?tag=" + data);
    },
    makePDF() {
      window.html2canvas = html2canvas;
      let that = this;
      let pdf = new jsPDF("p", "mm", "a4");
      let canvas = pdf.canvas;
      const pageWidth = 210;
      const pageHeight = 295;
      canvas.width = pageWidth;

      let ele = document.querySelector("body");
      let width = ele.offsetWidth;
      let height = ele.offsetHeight;
      let imgHeight = (pageWidth * height) / width;

      if (!ele) {
        // console.warn(selector + " is not exist");
        return false;
      }

      html2canvas(ele).then(function (canvas) {
        let position = 0;

        const imgData = canvas.toDataURL("image/png");
        pdf.addImage(
          imgData,
          "png",
          0,
          position,
          pageWidth,
          imgHeight,
          undefined,
          "slow"
        );

        let heightLeft = imgHeight;
        heightLeft -= pageHeight;
        while (heightLeft >= 0) {
          position = heightLeft - imgHeight;
          pdf.addPage();
          pdf.addImage(imgData, "png", 0, position, pageWidth, imgHeight);
          heightLeft -= pageHeight;
        }
        pdf.save(that.propTitle.toLowerCase() + ".pdf");
      });
    },
    onInput(event) {
      this.writeCom = event.data;
    },
    getLike() {
      axios
        .put(process.env.VUE_APP_API_URL + "like/", {
          postid: this.$route.params.id,
          useremail: this.$store.state.useremail,
          username: this.$store.state.username,
        })
        .then(({ data }) => {
          if (data.data == "success" && data.object) {
            this.liked = true;
          } else {
            this.liked = false;
          }
        });
    },
    getRecentView() {
      axios
        .post(process.env.VUE_APP_API_URL + "getRecentView", {
          useremail: this.$store.state.useremail,
        })
        .then(({ data }) => {
          this.recentViews = data.object;
          if (this.recentViews.length >5) {
            this.recentViews.splice(5)
          }   
        });
    },

    getPost() {
      window.scrollTo(0, 0);
      var postid = this.$route.params.id;
      if (this.$store.state.useremail) {
        axios
          .post(process.env.VUE_APP_API_URL + "selectpost", {
            postid: postid,
            useremail: this.$store.state.useremail,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.title = data.object.title;
              this.username = data.object.username;
              this.useremail = data.object.useremail;
              this.contents = data.object.contents;
              this.createdat = data.object.createdat;
              //console.log(this.contents);
            } else {
              // console.log(data);
            }
          });
      } else {
        axios
          .post(process.env.VUE_APP_API_URL + "selectpost", {
            postid: postid,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.title = data.object.title;
              this.username = data.object.username;
              this.useremail = data.object.useremail;
              this.contents = data.object.contents;
              this.createdat = data.object.createdat;
            } else {
              // console.log(data);
            }
          });
      }
    },
    getRelativePost() {
      axios
        .post(process.env.VUE_APP_API_URL + "getPopluarpostrelated", {
          postid: this.$route.params.id,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.relativePost = [];
            this.relativePostId = [];
            data.object.forEach((element) => {
              element.posts.forEach((post) => {
                // 자신의 포스트가 아니고 중복된 포스트 제거
                if (
                  post.postid != this.$route.params.id &&
                  this.relativePostId.indexOf(post.postid) == -1
                ) {
                  post.tag = element.hashtagname;
                  if (post.title.length > 25)
                    post.title = post.title.substring(0, 25) + "...";
                  this.relativePost.push(post);
                  this.relativePostId.push(post.postid);
                }
              });
            });
            if (this.relativePost.length > 5)
              this.relativePost = this.relativePost.slice(0, 5);
          } else {
            // console.log(data);
          }
        });
    },
    mvDetail(data) {
      this.$router.push("/post/detail/" + data);
    },
    mvList() {
      this.$router.push("/post/list");
    },
    deletePost() {
      axios
        .delete(process.env.VUE_APP_API_URL + "post", {
          params: {
            postid: this.$route.params.id,
          },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("삭제 완료!!");
            this.$router.push("/post/list");
          } else {
            // console.log(data);
          }
        });
    },
    modifyPost() {
      // console.log(this.$route.params.id);
      this.$router.push("/post/modify/" + this.$route.params.id);
    },
    shareOpen() {
      document.getElementsByClassName("share-icon")[0].style.display = "";
      // console.log(process.env.VUE_APP_API_URL + this.$route.path.substring(1));
    },
    shareClose() {
      document.getElementsByClassName("share-icon")[0].style.display = "none";
      // console.log(process.env.VUE_APP_API_URL + this.$route.path.substring(1));
    },
    share(data) {
      var copyUrl = document.URL;
      // console.log(copyUrl);
      switch (data) {
        case "twit":
          copyUrl =
            "https://twitter.com/intent/tweet?text=페이지제목:" +
            this.title +
            "&url=" +
            copyUrl;
          break;
        case "face":
          copyUrl = "http://www.facebook.com/sharer/sharer.php?u=" + copyUrl;
          break;
        case "kakaotalk":
          break;
        case "kakaostory":
          copyUrl = "https://story.kakao.com/share?url=" + copyUrl;
          break;
        case "insta":
          break;
        default:
          var IE = document.all ? true : false;
          if (IE) {
            if (confirm("주소가 복사되었습니다."))
              window.clipboardData.setData("Text", copyUrl);
          } else {
            prompt("이 주소를 복사해주세요.", copyUrl);
          }

          return;
      }
      window.open(
        copyUrl,
        "",
        "width=600,height=300,top=100,left=100,scrollbars=yes"
      );
    },
    writeComment() {
      axios
        .post(process.env.VUE_APP_API_URL + "comments/", {
          postid: this.$route.params.id,
          contents: this.writeCom,
          useremail: this.$store.state.useremail,
          username: this.$store.state.username,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.getComment();
            this.writeCom = "";
          } else {
            // console.log(data);
          }
        });
    },
    getComment() {
      axios
        .get(process.env.VUE_APP_API_URL + "comments/" + this.$route.params.id)
        .then(({ data }) => {
          if (data.data == "success") {
            this.comments = data.object;
          } else {
            this.comments = "";
            // console.log(data);
          }
        });
    },
    deleteComment(data) {
      axios
        .delete(process.env.VUE_APP_API_URL + "comments", {
          params: { commentsid: data },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.getComment();
          } else {
            // console.log(data);
          }
        });
    },
    updateComment(data) {
      // console.log(this.nComment);
      axios
        .put(process.env.VUE_APP_API_URL + "comments/", {
          commentsid: data.commentsid,
          contents: this.nComment,
          postid: data.postid,
          useremail: data.useremail,
          username: data.username,
          createdat: data.createdat,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.getComment();
          } else {
            // console.log(data);
          }
        });
      document.getElementById("td" + data.commentsid).style.display = "";
      document.getElementById(data.commentsid).type = "hidden";
      document.getElementById("open" + data.commentsid).style.display = "";
      document.getElementById("close" + data.commentsid).style.display = "none";
      // this.open();
    },
    open(data) {
      document.getElementsByClassName("comment-contents").forEach((element) => {
        element.style.display = "";
      });
      document.getElementsByClassName("inputCom").forEach((element) => {
        element.type = "hidden";
      });
      document.getElementsByClassName("openCom").forEach((element) => {
        element.style.display = "";
      });
      document.getElementsByClassName("submit").forEach((element) => {
        element.style.display = "none";
      });

      this.nComment = data.contents;
      document.getElementById("td" + data.commentsid).style.display = "none";
      document.getElementById(data.commentsid).type = "text";
      document.getElementById("open" + data.commentsid).style.display = "none";
      document.getElementById("close" + data.commentsid).style.display = "";
      this.beforeCom = data.commentsid;
    },
    like() {
      if (!this.liked) {
        axios
          .post(process.env.VUE_APP_API_URL + "like", {
            postid: this.$route.params.id,
            useremail: this.$store.state.useremail,
            username: this.$store.state.username,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.toast("게시글 좋아요");
              this.liked = true;
              this.getlikenumber();
            } else {
              // console.log(data);
            }
          });
      } else if (this.liked) {
        axios
          .delete(process.env.VUE_APP_API_URL + "like", {
            params: {
              postid: this.$route.params.id,
              useremail: this.$store.state.useremail,
            },
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.toast("좋아요 취소");
              this.liked = false;
              this.getlikenumber();
            } else {
              // console.log(data);
            }
          });
      }
    },
    
    getlikenumber() {
      axios
        .get(process.env.VUE_APP_API_URL + "like/" + this.$route.params.id)
        .then(({ data }) => {
          if (data.data == "success") {
            this.likenumber = data.object;
          } else {
            // console.log(data);
          }
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.postbox {
  border-top: 1px solid #d2dbe3;
  padding: 20px;
}

.postboxtitle {
  color: #a593e0;
}

.alt {
  text-align: left;
}

a {
  border-bottom: 0;
}
.detail {
  padding: 1em;
  * {
    word-break: break-all;
  }
  .detail-view * {
    padding: 0.2em;
  }
}

.detail-title {
  font-size: 27pt;
  margin-bottom: 10px;
  padding-bottom: 20px;
}
.detail-info {
  font-size: 13pt;
  padding-bottom: 15px;
  border-bottom: 1px solid grey;
  margin-bottom: 20px;
}

.detail-footer {
  text-align: right;
  padding: 20px;
  padding-top: 0px;
  border-bottom: 1px solid grey;
  position: relative;
  margin-bottom: 20px;
}
.detail .detail-btn {
  margin-left: 120px;
  float: right;
}
.share-icon {
  position: absolute;
  right: 0px;
  height: 100px;
  width: 170px;
  background-color: #fffff3;
  border: 1px solid #a593e0;
  border-radius: 0.5em;
}

.share-icon img {
  height: 20px;
  margin: 10px;
}
input[id="comment"] {
  width: 80%;
  border: 2px solid #aaa;
  border-radius: 4px;
  margin: 8px 0;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
  float: left;
}

.comment-username {
  // width: 120px;
  word-break: break-all;
  padding-right: 15px;
}

.comment-contents {
  word-break: break-all;
  // width: 70%;
}

table {
  width: 100%;
}
.detail-content .table tbody tr > * {
  border: 1px solid grey;
}
#modifyPost {
  margin-right: 25px;
}

#deletePost {
  text-align: right;
  margin-right: 20px;
}
.relative {
  margin-top: 60px;
  margin-left: 20px;
  width: 70%;
}

.relative-title:hover {
  color: #a593e0;
}
.emoji-wysiwyg-editor {
  width: 80%;
  border: 2px solid #aaa;
  border-radius: 4px;
  margin: 8px 0;
  outline: none;
  padding: 8px;
  box-sizing: border-box;
  transition: 0.3s;
  float: left;
}
.recent {
  width: 100%;
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

hr {
  display: block;
  unicode-bidi: isolate;
  margin-block-start: 0.5em;
  margin-block-end: 0.5em;
  margin-inline-start: auto;
  margin-inline-end: auto;
  overflow: hidden;
  border-style: inset;
  border-width: 1px;
}
a,
.relative-title,
.relative-tag {
  cursor: pointer;
}

td {
  text-align: left;
  margin: 0;
  padding: 5px 5px;
  font-size: 1rem;
  padding-left: 10px;
}

table {
  margin: 5px;
}
.co {
  width: 100%;
  tr {
    border: none;
  }
}
</style>
