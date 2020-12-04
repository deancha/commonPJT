<template>
  <div>
    <!--x 버튼 추가했는데 아직 기능 구현 안됨-->
    <div class="user wrap" id="login" style="height: auto">
      <div v-if="!issignup&&!isterms" class="container">
        <h2 style="text-align:center; padding-top: 3%; margin-bottom: 5%">
          Login
          <b-icon
            @click="$emit('close')"
            v-if="isModal"
            style="float:right; padding-right:5%; cursor:pointer"
            icon="x-circle"
          />
        </h2>
        <div class="row">
          <div class="col" style="padding-left:5%; padding-right:5%">
            <p>간편로그인</p>
            <div class="social-btn kakao btn" @click="kakaoLogin" style="position:relative">
              <img
                src="@/assets/img/kakaolink_btn_small.png"
                style="position: absolute;left: 5px;top: 4px;"
                height="33px"
              />
              <div style="padding-left : 37px">Login with KaKaoTalk</div>
            </div>
            <div class="social-btn github btn" @click="githubLogin" style="position:relative">
              <img
                src="@/assets/img/github-logo=32px.png"
                style="position: absolute;left: 5px;top: 4px;"
                height="30px"
              />
              <div style="padding-left : 37px">Login with Github</div>
            </div>
            <div class="social-btn naver btn" @click="naverLogin" style="position:relative">
              <img
                src="@/assets/img/naver-logo.png"
                style="position: absolute;left: 5px;top: 4px;"
                height="33px"
              />
              <div style="padding-left : 37px">Login with Naver</div>
            </div>
          </div>
          <div>
            <span class="vl-innertext">
              <b>or</b>
            </span>
          </div>

          <div class="col">
            <p>일반로그인</p>
            <input type="text" v-model="Login.email" id="email" placeholder="이메일을 입력해주세요" required />
            <input
              v-model="Login.password"
              type="password"
              id="password"
              username="password"
              placeholder="비밀번호를 입력해주세요"
              required
            />
            <div class="attention">*영문, 숫자 혼용 8자 이상 입력해주세요</div>
            <input type="submit" value="Login" @click="checkHandler" style="padding:0px;" />

            <div class="readytosignup" style="width:90%">
              <div style="float:left">아직 회원이 아니신가요?</div>
              <div class="gotosignup" style="float:right" @click="openSignupModal">회원가입</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="user wrap" id="terms" style="height: auto">
      <div v-if="isterms" class="container">
        <div class="terms">
          <div class="container">
            <h2>
              이용약관
              <b-icon class="closeterms" icon="x-circle" @click="termsclose" />
            </h2>
          </div>
          <div class="terms-container">
            <Terms />
          </div>
        </div>
      </div>
    </div>
    <div class="user wrap" id="signup" style="height: auto">
      <div v-if="issignup" class="container">
        <div class="row">
          <div class="col hero">
            <!-- <img
              src="@/assets/img/welcome.jpg"
              alt="멋진
            상원이"
              width="100%"
            />-->

            <div class="welcome">
              <h5>
                멋쟁이 상원이처럼
                <br />블로그에 오신 것을 환영합니다.
                <br />
              </h5>
              <br />기억보단 기록을!!!
              <br />코딩 공부한 내용을
              <br />블로그에 기록하고 있어요
              <br />
              <br />저는 처음에 코딩을 시작할 때
              <br />어디서부터 시작할 지 막막했어요
              <br />어떻게 공부하면 좋을 지
              <br />코딩 커리큘럼도 소개해드립니다
              <br />
              <br />열심히 공부해서 같이
              <br />멋진 개발자가 됩시다:)
            </div>
          </div>

          <!-- <div class="vl">
          <span class="vl-innertext"><b>or</b> </span>
          </div>-->
          <div class="col">
            <div style="width:90% ;margin-top:5%; margin-bottom:20%; padding-left: 10%">
              <h2 style="float:left">Signup</h2>
              <b-icon style="float:right; cursor:pointer" icon="x-circle" @click="$emit('close')" />
            </div>
            <div class="signup">
              <p style="font-weight:bold ; ">회원가입</p>
              <input
                type="text"
                :class="{ 'border-red': !isNickname }"
                v-model="nickName"
                id="nickName"
                placeholder="닉네임을 입력해주세요"
                maxlength="8"
                required
                @change="nickNameVal"
              />
              <input
                type="text"
                :class="{ 'border-red': !isEmail }"
                v-model="email"
                id="email"
                placeholder="이메일을 입력해주세요"
                required
                @change="emailVal"
              />
              <div style="width:90%; ">
                <input
                  class="code"
                  :class="{ 'border-red': !isCheckEmailCode }"
                  v-if="this.isEmailCodeSend"
                  style="width:40%;  border:0px; border-bottom: 1px solid grey"
                  v-model="emailCode"
                  @change="codeVal"
                />
                <button class="sendnumber small" @click="emailValSend">인증번호 보내기</button>
              </div>

              <input
                :class="{ 'border-red': !isPwd }"
                v-model="password"
                id="password"
                username="password"
                :type="passwordType"
                placeholder="비밀번호를 입력해주세요"
                required
                @change="pwdVal"
              />
              <div class="attention">*영문, 숫자 혼용 8자 이상 입력해주세요</div>
              <input
                :class="{ 'border-red': !isPwdConfirm }"
                v-model="passwordConfirm"
                :type="passwordConfirmType"
                id="password-confirm"
                username="password"
                placeholder="비밀번호를 한번 더 입력해주세요"
                required
                @change="pwdConfirmVal"
              />
              <div class="attention">*영문, 숫자 혼용 8자 이상 입력해주세요</div>
              <!--약관보기 연결 추가하기. 일단은 로그인으로 연결됨-->
              <div class="check-term-container" style="width:90%; ">
                <div class="checkbox-container">
                  <input v-model="isTerm" type="checkbox" id="check-term" />
                  <label for="check-term">약관에 동의합니다</label>
                  <div class="gototerms" style="float:right ;" @click="openTermsModal">약관보기</div>
                </div>
              </div>
              <input type="submit" value="Signup" @click="checkJoin" style="padding:0px;" />
              <div style="width:90%; ">
                <div style="float:left ">로그인하시겠어요?</div>
                <div class="gotologin" style="float:right ;" @click="openLoginModal">로그인</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import constants from "../../lib/constants";
import axios from "axios";
// import Signup from "./Signup.vue";
import MyModal from "@/components/common/MyModal.vue";

import Terms from "@/components/common/Terms.vue";

export default {
  name: "Login",
  components: { MyModal, Terms },
  data: () => {
    return {
      Login: {
        constants,
        email: "",
        password: "",
        loginModal: false,
      },
      color: "white",
      email: "",
      nickName: "",
      password: "",
      passwordConfirm: "",
      isTerm: false, // 약관동의
      isEmail: false, // 올바른 이메일 형식 + 중복하지 않은 이메일
      isNickname: false, // 중복하지 않은 닉네임
      passwordType: "password",
      passwordConfirmType: "password",
      isEmailCodeSend: false, // 이메일 인증 번호를 전송한지 체크
      emailCode: "", // 인증 번호
      checkEmailCode: "", // 받아온 인증 번호
      isCheckEmailCode: false, // 인증 번호가 일치하는지 체크
      isPwd: false, // 비밀번호 올바른 형식
      isPwdConfirm: false,
      issignup: false,
      islogin: false,
      isModal: true,
      isterms: false,
    };
  },
  computed: {},
  created() {},

  watch: {},
  methods: {
    termsclose() {
      this.isterms = false;
      this.issignup = true;
      openTermsModal();
    },
    toast(content, toaster = "b-toaster-top-center", append = false) {
      this.counter++;
      this.$bvToast.toast(content, {
        title: `멋쟁이 상원처럼 알림`,
        toaster: toaster,
        solid: true,
        appendToast: append,
      });
    },
    naverLogin() {
      axios.get(process.env.VUE_APP_API_URL + "naver").then(({ data }) => {
        if (data.apiURL) {
          location.href = data.apiURL;
        }
      });
    },
    kakaoLogin() {
      // console.log("here");
      axios.get(process.env.VUE_APP_API_URL + "kakao").then(({ data }) => {
        if (data.apiURL) {
          location.href = data.apiURL;
        }
      });
    },
    googleLogin() {
      // console.log(process.env.VUE_APP_API_URL + "kakao");
      axios.get(process.env.VUE_APP_API_URL + "google").then(({ data }) => {
        if (data.apiURL) {
          location.href = data.apiURL;
        }
      });
    },
    githubLogin() {
      // console.log(process.env.VUE_APP_API_URL + "kakao");
      axios.get(process.env.VUE_APP_API_URL + "github").then(({ data }) => {
        if (data.apiURL) {
          location.href = data.apiURL;
        }
      });
    },
    openSignupModal() {
      this.issignup = true;
    },
    openLoginModal() {
      this.issignup = false;
    },
    openTermsModal() {
      this.isterms = true;
      this.issignup = false;
    },

    closeModal() {
      this.modal = false;
    },

    checkHandler() {
      if (!this.Login.email) {
        this.toast("이메일을 입력해주세요.");
      } else if (!this.Login.password) {
        this.toast("패스워드를 입력해주세요.");
      } else this.login();
    },
    login() {
      axios
        .post(process.env.VUE_APP_API_URL + "account/socialCheck", {
          issocial: null,
          useremail: this.Login.email,
        })
        .then(({ data }) => {
          if (data.check == "1") {
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
            }
          } else if (data.check == "2") {
            axios
              .post(process.env.VUE_APP_API_URL + "account/login", {
                useremail: this.Login.email,
                password: this.Login.password,
              })
              .then(({ data }) => {
                if (data.data == "success") {
                  this.toast("로그인 되었습니다.");
                  this.$store.commit("loginToken", data.object);
                  // console.log(data.object);
                  this.$emit("close");
                  if (this.$route.path != "/") this.$router.push("/");
                } else {
                  this.toast("비밀번호가 틀렸습니다.");
                }
              })
              .catch(() => {
                if (this.$route.path != "/500")
                  this.$router.push("/500/로그인에 실패했습니다.");
              });
          } else this.toast("존재하지 않는 이메일입니다.");
          {
          }
        });
    },
    checkJoin() {
      if (!this.isPwd) {
        this.toast("비밀번호가 올바르지 않습니다.");
      } else if (!this.isPwdConfirm) {
        this.toast("비밀번호가 일치하지 않습니다.");
      } else if (!this.isNickname) {
        this.toast("닉네임이 올바르지 않습니다.");
      } else if (!this.isEmail) {
        this.toast("이메일이 올바르지 않습니다.");
      } else if (!this.isCheckEmailCode) {
        this.toast("이메일 인증 번호를 확인해주세요.");
      } else if (!this.isTerm) {
        this.toast("약관에 동의해주세요.");
      } else {
        this.join();
      }
    },
    join() {
      // form 검증
      axios
        .post(process.env.VUE_APP_API_URL + "account/signup", {
          username: this.nickName,
          password: this.password,
          useremail: this.email,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("회원가입에 성공하였습니다.");
            this.$emit("close");

            if (this.$route.path != "/") this.$router.push("/");
          } else {
            this.toast("실패");
            this.$router.push("/500");
          }
        });
    },
    emailValidate() {
      let regex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
      return regex.test(this.email);
    },
    passwordValidate() {
      let regex = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}$/;
      return regex.test(this.password);
    },
    emailValSend() {
      if (this.email && this.isEmail) {
        // axios로 코드 전송
        this.isEmailCodeSend = true; // 이메일을 보냈다는 뜻
        axios
          .post(process.env.VUE_APP_API_URL + "account/SendCheckEmail", {
            username: this.nickName,
            useremail: this.email,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.toast("인증 번호 발송에 성공했습니다.");
              this.checkEmailCode = data.RandomNumber;
            } else {
              this.toast("인증 번호 발송에 실패했습니다.");
            }
          });
      } else this.toast("이메일을 확인해주세요.");
    },
    nickNameVal() {
      // console.log(this.nickName);
      axios
        .post(process.env.VUE_APP_API_URL + "account/usernameDuplicateCheck", {
          username: this.nickName,
        })
        .then(({ data }) => {
          // console.log(data);
          if (data.data == "success") {
            this.isNickname = true;
          } else {
            this.isNickname = false;
          }
        })
        .catch(function (error) {
          // console.log("fail");
          this.isNickname = false;
        });
    },
    emailVal() {
      if (this.emailValidate()) {
        axios
          .post(process.env.VUE_APP_API_URL + "account/EmailDuplicateCheck", {
            useremail: this.email,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.isEmail = true;
              // console.log("중복x");
            } else {
              this.isEmail = false;
              // console.log("중복o");
            }
          })
          .catch(function (error) {
            this.isEmail = false;
          });
      } else {
        this.isEmail = false;
      }
    },
    pwdVal() {
      if (!this.passwordValidate()) this.isPwd = false;
      else this.isPwd = true;
    },
    pwdConfirmVal() {
      if (this.password != this.passwordConfirm) this.isPwdConfirm = false;
      else this.isPwdConfirm = true;
    },
    codeVal() {
      if (this.checkEmailCode && this.checkEmailCode == this.emailCode.trim()) {
        this.isCheckEmailCode = true;
      } else this.isCheckEmailCode = false;
    },
    emitClose() {
      this.$emit("close");
    },
    // checkTerm() {
    //   this.isTerm = !this.isTerm
    // }
  },
};
</script>

<style lang="scss" scoped>
/* .container {
  width: 90%;
  margin: 10px auto;
} */

.checkbox-container {
  position: relative;
  padding-top: 20px;
  padding-bottom: 5px;
}

.checkbox-container input[type="checkbox"] {
  position: absolute;

  width: 1px;

  height: 1px;

  padding: 0;

  margin: -1px;

  overflow: hidden;

  clip: rect(0, 0, 0, 0);

  border: 0;
}

.checkbox-container input[type="checkbox"] + label {
  display: inline-block;
  position: relative;
  cursor: pointer;
  user-select: none;
  margin: 0;
}

.checkbox-container input[type="checkbox"] + label:before {
  content: " ";
  display: inline-block;
  width: 18px;
  height: 18px;
  line-height: 18px;
  margin: -2px 8px 0 0;
  text-align: center;
  vertical-align: middle;
  background: #fafafa;
  border: 1px solid #cacece;
  border-radius: 3px;
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.05),
    inset 0px -15px 10px -12px rgba(0, 0, 0, 0.05);
}

.checkbox-container input[type="checkbox"] + label:active:before,
.checkbox-container input[type="checkbox"]:checked + label:active:before {
  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.05),
    inset 0px 1px 3px rgba(0, 0, 0, 0.1);
}

.checkbox-container input[type="checkbox"]:checked + label:before {
  content: "\2713";
  color: #a593e0;

  text-shadow: 1px 1px white;

  background: #f1f4ff;

  border-color: #adb8c0;

  box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.05),
    inset 0px -15px 10px -12px rgba(0, 0, 0, 0.05);
}

.border-red {
  border: 2px solid darkred;
}
/* .t {
  padding-left: 5%;
} */
.social {
  float: left;
  width: 30%;
  padding-left: 2%;
}
.or {
  float: left;
  width: 10%;
  margin-top: 20%;
  padding-left: 4%;
  background-color: #f1f1f1;
}
.login {
  float: right;
  width: 60%;
  /* padding-right: 2%; */
}
.gotosignup {
  cursor: pointer;
}
.gotologin {
  cursor: pointer;
}
.sendnumber {
  cursor: pointer;
}
.attention {
  float: left;
  font-size: 0.8em;
}
/* .gotosignup {
  margin-top: 10px;
  margin-bottom: 10px;
} */
.gototerms {
  cursor: pointer;
}
.check-term-container {
  margin-top: 5px;
}

.terms {
  overflow: scroll;
}

.terms h2 {
  text-align: center;
  padding-top: 3%;
  margin-bottom: 5%;
  padding-right: 20px;
}

.terms-container {
  padding: 20px;
  height: 300px;
}

.closeterms {
  float: right;
  cursor: pointer;
}
.wrap {
  width: 100%;
  float: left;
}
/* h2 {
  margin-top: 50px;
  margin-bottom: 50px;
} */
p {
  float: left;
  font-weight: bold;
  margin-bottom: 10px;
}

.container {
  position: relative;
  max-width: 700px;
  margin: auto;
  background-color: #f2f2f2;
  padding: 0;
  border: 0;
  /* padding: 20px 0 30px 0; */
}
form {
  border-radius: 20px;
}
input,
.btn {
  width: 90%;
  padding: 12px;
  border: none;
  border-radius: 4px;
  margin: 7px 0;
  opacity: 0.85;
  display: inline-block;
  font-size: 12px;
  line-height: 20px;
  text-decoration: none;
}

.code {
  -moz-appearance: none;
  -webkit-appearance: none;
  -ms-appearance: none;
  appearance: none;
  -moz-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
  -webkit-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
  -ms-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
  transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
  background-color: transparent;
  border-radius: 0.375em;
  border: 0;
  box-shadow: inset 0 0 0 2px #a593e0;
  color: #a593e0 !important;
  cursor: pointer;
  display: inline-block;
  font-size: 0.7em;
  font-weight: 700;
  height: 3.5em;
  letter-spacing: 0.075em;
  line-height: 3.5em;
  padding: 0 2.25em;
  text-align: center;
  text-decoration: none;
  text-transform: uppercase;
  white-space: nowrap;
  width: 40%;
}

input:hover,
.btn:hover {
  opacity: 1;
  box-shadow: 0 0 10px gray;
}

.small {
  padding-right: 9px;
  padding-left: 9px;
  font-size: 0.7rem;
  line-height: 15px;
  float: right;
}

input .verification {
  padding-right: 9px;
  padding-left: 9px;
  font-size: 0.7rem;
  line-height: 15px;
  float: left;
}

.social-btn {
  padding: 0;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
  line-height: 40px;
}
.kakao {
  background-color: #fee500;
  color: #3c1c1c;
  font-weight: 900;
}

.github {
  background-color: white;
  color: #171516;
  font-weight: 900;
}
.naver {
  background-color: #1bc904;
  color: white;
}

.kakao.btn {
  padding: 0;
  align-items: center;
  justify-content: center;
}

.github.btn {
  padding: 0;
  align-items: center;
  justify-content: center;
}
input[type="submit"] {
  background-color: #9a89d3;
  color: white !important;
  cursor: pointer;
  margin-top: 5px;
}
input[type="submit"]:hover {
  background-color: #566270;
}
.col {
  float: left;
  width: 50%;
  margin: auto;
  /* padding: 0 50px; */
  margin-top: 10px;
  padding: 0;
  margin: 0;
  padding-bottom: 1rem;
}
.row {
  margin: 0;
  padding: 0;
}
.row:after {
  content: "";
  display: table;
  clear: both;
  padding: 0;
}

.signup {
  padding-left: 10%;
}

.vl {
  position: absolute;
  left: 50%;
  transform: translate(-50%);
  border: 2px solid #ddd;
  height: 250px;
}

.vl-innertext {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #f1f1f1;
  border: 1px solid #ccc;
  border-radius: 50%;
  padding: 8px 10px;
}

.hide-md-lg {
  display: none;
}

.hero {
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  text-align: center;
  justify-content: center;
  background-image: url("~@/assets/img/welcome.jpg");
  background-size: cover;
}

.hero::before {
  content: "";
  position: absolute;
  top: 0px;
  right: 0px;
  bottom: 0px;
  left: 0px;
  background-color: rgba(0, 0, 0, 0.7);
}

.welcome {
  position: relative;
  color: #ffffff;
  font-size: 1rem;
  line-height: 1.5;
  text-align: center;
  display: inline-block;
  width: 80%;
  margin: 5% 0;
}

h5 {
  font-weight: bold;
  color: #ffffff;
  line-height: 1.5;
}
@media screen and (max-width: 650px) {
  .social-btn {
    img {
      display: none;
    }
    div {
      padding-left: 0px !important;
    }
  }
}
@media screen and (max-width: 650px) {
  .col {
    width: 100%;
    margin-top: 0;
    border: 0;
  }

  .hide-md-lg {
    display: block;
    text-align: center;
  }
  .col .sang {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    /*     background-image: url(https://placeimg.com/300/400/animals/grayscale);
 */
    border: 0;
  }
}
</style>
