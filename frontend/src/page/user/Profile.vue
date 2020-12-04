<template>
  <div class="container">
    <b-tabs content-class="mt-3" fill>
      <b-tab title="회원정보" active>
        <div class="card">
          <div class="profile-sidebar">
            <div class="form-group">
              <!-- <UserProfileComponent /> -->
              <div>
                <b-avatar id="popover-target-1" size="6em" :src="this.userInfo.imagePreview"></b-avatar>

                <!-- 아바타에 팝오버시  -->
                <b-popover target="popover-target-1" triggers="hover" placement="top">
                  <template v-slot:title>프로필 이미지 변경</template>
                  <b-list-group>
                    <b-list-group-item button @click="openUpload">PC에서 이미지 선택</b-list-group-item>
                    <b-list-group-item button @click="defaultImg">기본 이미지로 변경</b-list-group-item>
                  </b-list-group>
                </b-popover>

                <input
                  name="image"
                  type="file"
                  id="file-field"
                  v-on:change="updatePreview"
                  style="display:none;"
                />
              </div>
            </div>
          </div>
          <div class="profile-main">
            <div class="row">
              <div class="col-20">
                <label for="fname">UserName</label>
              </div>
              <div class="col-75">
                <input
                  type="text"
                  id="fname"
                  v-model="userInfo.username"
                  name="username"
                  @blur="validate('name')"
                  maxlength="8"
                  placeholder="이름은 8자 이하로 설정해주세요."
                />
              </div>
            </div>

            <div class="row">
              <div class="col-20">
                <label for="phone">Phone</label>
              </div>
              <div class="col-75">
                <input
                  type="text"
                  id="phone"
                  v-model="userInfo.phonenumber"
                  name="phonenumber"
                  placeholder="010-0000-0000"
                  @blur="validate('phone')"
                />
              </div>
            </div>

            <div class="row">
              <div class="col-20">
                <label for="femail">Email</label>
              </div>
              <div class="col-75">
                <input
                  type="text"
                  id="femail"
                  name="email"
                  :value="$store.state.useremail"
                  readonly
                  style="background-color: lightgrey"
                />
              </div>
            </div>

            <div class="row">
              <div class="col-20">
                <label for="fpw">Password</label>
              </div>
              <div class="col-75">
                <input
                  type="password"
                  id="fpw"
                  v-model="userInfo.password"
                  name="password"
                  @blur="validate('password')"
                  placeholder="숫자+영문자 8자리 이상으로 조합해주세요."
                />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-20">
              <label for="gender">Gender</label>
            </div>
            <div class="col-75" style="text-align: left">
              <b-form-group>
                <b-form-radio-group v-model="userInfo.gender" name="radio-sub-component">
                  <b-form-radio value="M">남자</b-form-radio>
                  <b-form-radio value="W">여자</b-form-radio>
                  <b-form-radio value="null">선택하지 않음</b-form-radio>
                </b-form-radio-group>
              </b-form-group>
              <!-- <input
                type="checkbox"
                id="M"
                v-model="userInfo.gender[0]"
                @change="changeGender('M')"
              />
              <label for="M">남자</label>
              <input
                type="checkbox"
                id="W"
                v-model="userInfo.gender[1]"
                @change="changeGender('W')"
              />
              <label for="W">여자</label>
              <input
                type="checkbox"
                id="null"
                v-model="userInfo.gender[2]"
                @change="changeGender('null')"
              />
              <label for="null">선택하지않음</label>-->
            </div>
          </div>

          <div class="row">
            <div class="col-20">
              <label for="age">Age</label>
            </div>
            <div class="col-20">
              <input
                type="text"
                id="age"
                name="age"
                v-model="userInfo.age"
                @blur="validate('age')"
                placeholder="숫자만 입력해주세요"
              />
            </div>
          </div>

          <div class="row">
            <div class="col-20">
              <label for="subject">Introduction</label>
            </div>
            <div class="col-75">
              <textarea
                id="subject"
                v-model="userInfo.content"
                name="subject"
                style="height:200px"
                maxlength="300"
              ></textarea>
            </div>
          </div>
          <br />
          <div style="text-align:center;">
            <button class="btnn" @click="updateUserInfo">수정하기</button> &nbsp;&nbsp;
            <button class="btnn primary" @click="leave">탈퇴하기</button>
          </div>
        </div>
      </b-tab>
      <!-- <TestSlider /> -->
      <b-tab v-if="$store.state.isAdmin" title="포스트">
        <TablePagination />
      </b-tab>
      <b-tab title="좋아요">
        <TableLikePagination />
      </b-tab>
      <b-tab title="댓글">
        <TableCommentPagination />
      </b-tab>
    </b-tabs>
  </div>
</template>

<script>
import axios from "axios";
import UserProfileComponent from "../../components/common/UserProfileComponent.vue";

import PaginatedList from "../../components/common/PaginatedList.vue";
import PaginatedLikeList from "../../components/common/PaginatedLikeList.vue";
import TablePagination from "../../components/common/TablePagination.vue";
import TableLikePagination from "../../components/common/TableLikePagination.vue";
import TableCommentPagination from "../../components/common/TableCommentPagination.vue";

import firebase from "firebase";

var config = {
  apiKey: "AIzaSyD5YkPZU6lkj5gUxl4iiQTOaiuTUTI8MaM",
  authDomain: "likesangwon-f516a.firebaseapp.com",
  projectId: "likesangwon-f516a",
  storageBucket: "gs://likesangwon-f516a.appspot.com/",
};

firebase.initializeApp(config);

export default {
  name: "Profile",
  components: {
    UserProfileComponent,
    PaginatedList,
    PaginatedLikeList,
    TablePagination,
    TableLikePagination,
    TableCommentPagination,
    // TestSlider,
  },
  created() {
    this.getUserInfo();
  },

  data() {
    return {
      username: "",
      userInfo: {
        username: "",
        useremail: this.$store.state.useremail,
        profileimg: "",
        imagePreview: "",
        content: "",
        introduction: "",
        password: "",
        phonenumber: "",
        isadmin: "",
        issocial: "",
        gender: "",
        age: 0,
      },
      valid: {
        isName: true,
        isPw: true,
      },
      imgfile: "",
    };
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
    defaultImg() {
      this.userInfo.imagePreview = "";
      this.userInfo.profileimg = "";
    },
    sendImgToFB(file) {
      //단일파일만
      var storageRef = firebase.storage().ref();
      storageRef
        .child("profile/" + file.name)
        .put(file)
        .then(function (snapshot) {
          // console.log("Uploaded a blob or file!");
        });
    },
    getImgFromFB(file) {
      var storageRef = firebase.storage().ref();
      storageRef
        .child("profile/" + file.name)
        .getDownloadURL()
        .then((url) => {
          // console.log("url :" + url);
          this.userInfo.profileimg = url;
        });
    },
    openUpload() {
      document.getElementById("file-field").click();
      // console.log("profileimg : " + this.userInfo.profileimg);
    },

    //프로필이미지 클릭하면
    updatePreview(e) {
      this.imgfile = e.target.files[0];
      var reader = new FileReader();
      reader.onload = (e) => {
        this.userInfo.imagePreview = e.target.result;
      };
      reader.readAsDataURL(this.imgfile);

      if (typeof this.imgfile.name == "undefined") {
        // console.log("profileimg를 바꾸지않았습니다");
      } else {
        // console.log("imgfile : " + this.imgfile.name);
        this.sendImgToFB(this.imgfile);
        this.getImgFromFB(this.imgfile);
      }
    },

    getUserInfo() {
      axios
        .get(
          process.env.VUE_APP_API_URL + "account/" + this.$store.state.useremail
        )
        .then(({ data }) => {
          if (data.data == "success") {
            this.username = data.object.username;
            this.userInfo.username = data.object.username;
            this.userInfo.phonenumber = data.object.phonenumber;
            this.userInfo.password = data.object.password;
            this.userInfo.age = data.object.age;
            this.userInfo.profileimg = data.object.profileimg;
            this.userInfo.imagePreview = data.object.profileimg;
            // console.log("profile img : " +this.userInfo.profileimg);
            this.userInfo.content = data.object.introduction;
            this.userInfo.isadmin = data.object.isadmin;
            this.userInfo.issocial = data.object.issocial;
            this.userInfo.gender = data.object.gender;
          } else {
            this.$router.push("/500");
          }
        });
    },
    imgUpload(files) {
      var mountainsRef = storageRef.child("profile/" + files.name);
      //file : 이미지 파일
      mountainsRef.put(files).then(function (snapshot) {
        // console.log("Uploaded a blob or file!");
      });
    },
    //수정버튼을 눌렀을 때
    updateUserInfo() {
      if (!this.valid.isName) {
        this.toast("이름을 다시 설정해주세요.");
        return;
      } else if (!this.valid.isPw) {
        this.toast("비밀번호를 다시 설정해주세요.");
        return;
      }

      // console.log(this.userInfo.profileimg);
      this.$store.commit("loginToken", this.userInfo);
      axios
        .put(process.env.VUE_APP_API_URL + "account", {
          useremail: this.$store.state.useremail,
          username: this.userInfo.username,
          password: this.userInfo.password,
          age: this.userInfo.age,
          profileimg: this.userInfo.profileimg,
          gender: this.userInfo.gender,
          introduction: this.userInfo.content,
          phonenumber: this.userInfo.phonenumber,
          isadmin: this.userInfo.isadmin,
          issocial: this.userInfo.issocial,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            // console.log(data.object);
            this.toast("회원 정보가 수정되었습니다.");
            this.$store.commit("loginToken", data.object);
          } else {
            this.$router.push("/500");
          }
        });
    },
    leave() {
      axios
        .delete(process.env.VUE_APP_API_URL + "account", {
          params: { useremail: this.$store.state.useremail },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.$store.commit("logout");
            this.toast("탈퇴되었습니다.");
            // console.log("탈퇴");
            this.$router.push("/");
          } else {
            this.$router.push("/500");
          }
        });
    },
    changeGender(data) {
      for (var i = 0; i < 3; i++) {
        this.userInfo.gender[i] = false;
      }
      switch (data) {
        case "M":
          this.userInfo.gender[0] = true;
          break;
        case "W":
          this.userInfo.gender[1] = true;
          break;
        default:
          this.userInfo.gender[2] = true;
          break;
      }
    },
    validate(mode) {
      var regex;
      switch (mode) {
        case "name":
          if (this.username != this.userInfo.username) {
            axios
              .post(
                process.env.VUE_APP_API_URL + "account/usernameDuplicateCheck",
                { username: this.userInfo.username }
              )
              .then(({ data }) => {
                if (data.data == "success") {
                  this.valid.isName = true;
                } else {
                  this.toast("중복된 닉네임입니다.");
                  this.userInfo.username = this.username;
                  this.valid.isName = false;
                }
              });
          }
          break;
        case "phone":
          regex = /^\d{2,3}-\d{3,4}-\d{4}$/;
          if (!regex.test(this.userInfo.phonenumber)) {
            this.toast("010-0000-0000 형태로 입력해주세요.");
            this.userInfo.phonenumber = null;
          }
          break;
        case "password":
          regex = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}$/;
          if (!regex.test(this.userInfo.password)) {
            this.toast("숫자+영문자 8자리 이상으로 조합해주세요.");
            this.userInfo.password = "";
            this.valid.isPw = false;
          } else this.valid.isPw = true;
          break;
        case "age":
          regex = /^[0-9]*$/;
          if (!regex.test(this.userInfo.age)) {
            this.toast("나이는 숫자만 입력해주세요.");
            this.userInfo.age = 0;
          } else {
            this.userInfo.age = Number(this.userInfo.age);
          }
          break;
      }
    },
  },
};
</script>

<style scoped>
input[type="password"],
select,
textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
  margin: 0 0 0 50;
}

button.leave {
  background-color: none;
  outline: none;
  border: none;
  box-shadow: none;
}

button.leave:hover {
  background-color: none !important;
  outline: none;
  border: none;
  box-shadow: none;
}

/* button:focus {
  outline: none;
} */
​ .container {
  display: block;
  width: 100vw;
  /* align-items: center;
  justify-content: center; */
}

.card {
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  width: 80%;
  padding: 20px;
  /* align-items: center;
  justify-content: center; */
  margin: 0 auto;
}

* {
  box-sizing: border-box;
}

.form-group {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  margin-top: 10px;
}

input[type="text"],
select,
textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
  margin: 0 0 0 50;
}

label {
  padding: 12px 12px 12px 0;
  display: inline-block;
  text-align: left;
}

.btn1 {
  padding: 6px 12px;
  font-size: 17px;
  font-weight: normal;
  line-height: 1.428571429;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  cursor: pointer;
  border: 1px solid transparent;
  border-radius: 4px;
}

input[type="submit"] {
  justify-content: center;
}

input[type="submit"]:hover {
  background-color: #f67878;
}

.col-20 {
  float: left;
  width: 20%;
  margin-top: 6px;
}

.col-75 {
  float: left;
  width: 75%;
  margin-top: 6px;
}

/* Clear floats after the columns */
.row:after {
  content: "";
  display: table;
  clear: both;
}

/* Responsive layout - when the screen is less than 600px wide, make the two columns stack on top of each other instead of next to each other */
@media screen and (max-width: 600px) {
  .col-25,
  .col-75,
  input[type="submit"] {
    width: 100%;
    margin-top: 0;
  }
}
</style>
