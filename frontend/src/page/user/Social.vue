<template>
  <div class="user" id="login">
    <div v-if="!signup">
      <h2>소셜 로그인 중 입니다.</h2>
    </div>
    <div v-if="signup">
      <div class="profile-main">
        <div class="row">
          <div class="col-20">
            <label for="fname">UserName</label>
          </div>
          <div class="col-75">
            <input
              :class="{ 'border-red': !isNickName }"
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
              :value="userInfo.useremail"
              readonly
              style="background-color: lightgrey"
            />
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-20">
          <label for="gender">Gender</label>
        </div>
        <div class="col-75" style="text-align: left">
          <input
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
          <label for="null">선택하지않음</label>
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
      <div style="text-align:center; margin-top: 30px; margin-bottom: 50px">
        <button @click="join">회원가입</button>
      </div>
    </div>
  </div>
</template>

<script>
import constants from "../../lib/constants";
import axios from "axios";

export default {
  components: {},
  created() {
    this.social = this.$route.query.type;
    this.accessToken = this.$route.query.access_token;
    switch (this.social) {
      case "naver":
        this.getNaver();
        break;
      case "kakao":
        this.getKakao();
        break;
      case "github":
        this.getGitHub();
        break;
      default:
        this.$router.push("/500: 소셜 로그인에 실패했습니다.");
    }
  },
  watch: {},
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
    getNaver() {
      axios
        .get(process.env.VUE_APP_API_URL + "naver/getProfile", {
          params: { access_token: this.accessToken },
        })
        .then(({ data }) => {
          this.userInfo.username = data.response.nickname;
          this.userInfo.useremail = data.response.email;
          this.userInfo.issocial = "N";
          if (
            this.userInfo.useremail == null ||
            !this.userInfo.useremail.trim()
          ) {
            this.toast("연동된 이메일이 없습니다. 다른 방법으로 로그인해주세요.");
            // 여기 로그아웃
            this.$router.push("/");
          } else {
            this.checkEmail();
          }
        });
    },
    getKakao() {
      axios
        .get(process.env.VUE_APP_API_URL + "kakao/getProfile", {
          params: { access_token: this.accessToken },
        })
        .then(({ data }) => {
          this.userInfo.username = data.kakao_account.profile.nickname;
          this.userInfo.useremail = data.kakao_account.email;
          this.userInfo.issocial = "K";
          if (
            this.userInfo.useremail == null ||
            !this.userInfo.useremail.trim()
          ) {
            this.toast("연동된 이메일이 없습니다. 다른 방법으로 로그인해주세요.");
            // 여기 로그아웃
            this.$router.push("/");
          } else {
            this.checkEmail();
          }
        });
    },
    getGitHub() {
      axios
        .get(process.env.VUE_APP_API_URL + "github/getProfile", {
          params: { access_token: this.accessToken },
        })
        .then(({ data }) => {
          this.userInfo.username = data.login;
          this.userInfo.useremail = data.email;
          this.userInfo.issocial = "H";
          if (
            this.userInfo.useremail == null ||
            !this.userInfo.useremail.trim()
          ) {
            this.toast("연동된 이메일이 없습니다. 다른 방법으로 로그인해주세요.");
            // 여기 깃헙 로그아웃
            this.$router.push("/");
          } else {
            this.checkEmail();
          }
        });
    },
    checkEmail() {
      axios
        .post(process.env.VUE_APP_API_URL + "account/socialCheck", {
          issocial: this.userInfo.issocial,
          useremail: this.userInfo.useremail,
        })
        .then(({ data }) => {
          if (data.check == "1") {
            // 다른 케이스로 로그인 되어있는 경우
            switch (data.socialtype) {
              case "K":
                
                this.toast("카카오로 회원가입이 되어있습니다.");
                break;
              case "N":
                this.toast("네이버로 회원가입이 되어있습니다.");
                break;
              case "GH":
                this.toast("깃허브로 회원가입이 되어있습니다.");
                break;
              default:
                this.toast(
                  "'" +
                    this.userInfo.useremail +
                    "'은 이미 가입되어 있는 이메일입니다. 로그인해주세요."
                );
                break;
            }
            // 로그아웃 시켜야함
            this.$router.push("/");
          } else if (data.check == "2") {
            // 로그인 가능
            this.userInfo = data.object;
            this.$store.commit("loginToken", this.userInfo);
            this.$store.commit("setToken", this.accessToken);
            this.toast("로그인 되었습니다.");
            this.$router.push("/");
          } else if (data.check == "0") {
            // 회원 가입 진행
            this.toast("회원 정보를 입력해주세요.");
            this.validate("name");
            this.signup = true;
          }
        });
    },
    validate(mode) {
      var regex;
      switch (mode) {
        case "name":
          axios
            .post(
              process.env.VUE_APP_API_URL + "account/usernameDuplicateCheck",
              { username: this.userInfo.username }
            )
            .then(({ data }) => {
              if (data.data == "success") {
                this.isNickName = true;
              } else {
                this.toast("중복된 닉네임입니다.");
                this.isNickName = false;
              }
            });
          break;
        case "phone":
          regex = /^\d{2,3}-\d{3,4}-\d{4}$/;
          if (!regex.test(this.userInfo.phonenumber)) {
            this.toast("010-0000-0000 형태로 입력해주세요.");
            this.userInfo.phonenumber = null;
          }
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
    join() {
      if (!this.isNickName) {
        this.toast("중복된 닉네임입니다!");
        return;
      }
      axios
        .post(process.env.VUE_APP_API_URL + "account/signup", {
          username: this.userInfo.username,
          useremail: this.userInfo.useremail,
          phonenumber:
            this.userInfo.phonenumber == "" ? null : this.userInfo.phonenumber,
          age: this.userInfo.age,
          gender: this.userInfo.gender[0]
            ? "M"
            : this.userInfo.gender[1]
            ? "W"
            : null,
          issocial: this.userInfo.issocial,
          password: Math.random()
            .toString(36)
            .substr(2, 11),
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("회원가입에 성공하였습니다.");
            this.$store.commit("loginToken", this.userInfo);
            this.$store.commit("setToken", this.accessToken);
            if (this.$route.path != "/") this.$router.push("/");
          } else {
            this.toast("실패");
            this.$router.push("/500");
          }
        });
    },
  },
  data: () => {
    return {
      userInfo: {
        username: "",
        useremail: "",
        phonenumber: "",
        age: 0,
        gender: [false, false, true],
        issocial: "",
        profileimg: "",
      },
      social: "",
      accesstoken: "",
      signup: false,
      isNickName: false,
    };
  },
};
</script>
<style scoped>
.border-red {
  border: 1.5px solid darkred !important;
}
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

​ .container {
  display: block;
  width: 100vw;
}

.card {
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  width: 80%;
  padding: 20px;
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

.btn {
  padding: 6px 12px;
  margin-top: 10px;
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

.row:after {
  content: "";
  display: table;
  clear: both;
}

@media screen and (max-width: 600px) {
  .col-25,
  .col-75,
  input[type="submit"] {
    width: 100%;
    margin-top: 0;
  }
}
</style>
