<template>
  <div
    class="curriculum"
    align="left"
    style="padding: 10px;width:1000px; height:620px; position:relative;overflow-y: auto"
  >
    <b-icon
      @click="$emit('close')"
      style="position:absolute; right:10px; top:10px; cursor:pointer"
      icon="x-circle"
    />
    <h2
      style="padding:10px 10px 0 10px; font-weight: 600;"
    >{{curriculummediumscale}} > {{ curriculumsmallscale }}</h2>
    <hr />
    <b-row style="width: 97%; margin: 0 auto 0 auto">
      <b-col cols="8">
        <div class="youtube" style="margin-bottom: 20px">
          <b-embed type="iframe" aspect="16by9" :src="link" allowfullscreen></b-embed>
        </div>
        <hr />
        <h4>강의 설명</h4>
        <h5>{{contents}}</h5>
        <div v-if="curFinish" style="text-align : center">
          <button
            v-if="curFinish == 'none'"
            class="primary btnn"
            id="StudyBtn"
            @click="study('ing')"
          >학습 참가</button>
          <button
            v-if="curFinish == 'ing'"
            class="primary btnn"
            style="margin-right: 15px"
            id="StudyBtn"
            @click="study('done')"
          >학습 완료</button>
          <button v-if="curFinish == 'ing'" class="btnn" id="StudyBtn" @click="cancel">학습 취소</button>
          <button v-if="curFinish == 'done'" class="btnn primary no-hover">학습 성공</button>
        </div>
      </b-col>
      <b-col cols="4" v-if="student">
        <ul style="margin-left:0">
          <li :class="{'grey' : isOne}">
            <div class="student" style="position: relative" @click="toggle(1)">
              '{{ curriculummediumscale }}' 명예의 전당
              <p style="width: 25px"></p>
              <img
                v-if="!this.isOne"
                src="@/assets/img/pp.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
              <img
                v-if="this.isOne"
                src="@/assets/img/pp2.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
            </div>
            <div class="list" v-if="this.isOne">
              <ul>
                <li v-for="st in student" :key="st.name">
                  <div v-if="st.rank == 1">
                    <span style="font-size:9pt">본 과정을 모두 학습한 유저입니다.</span>
                    <b-row style="text-align:center">
                      <b-col cols="7" style="padding:0" id="all">{{st.name}}</b-col>
                      <b-col cols="5" style="padding:0" id="all">
                        <img
                          src="@/assets/img/crown.png"
                          height="18px"
                          style="vertical-align: top;"
                        />
                      </b-col>
                      <b-popover target="all" triggers="hover" placement="top">
                        <p v-for="sc in scaleList" :key="sc" style="margin: 0; padding:0">{{ sc }}</p>
                      </b-popover>
                    </b-row>
                  </div>
                </li>
              </ul>
            </div>
          </li>
          <li>
            <div class="student" style="position: relative" @click="toggle(4)">
              '{{ curriculummediumscale }}' 학습 중
              <p style="width: 25px"></p>
              <img
                v-if="!this.isFour"
                src="@/assets/img/pp.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
              <img
                v-if="this.isFour"
                src="@/assets/img/pp2.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
            </div>
            <div class="list" v-if="this.isFour">
              <ul style="padding-left:0">
                <li v-for="st in student" :key="st.name" style="padding-left:0">
                  <div v-if="st.rank == 4" :id="st.name" style="text-align:center">
                    <p>{{st.name}}</p>
                    <b-popover :target="st.name" triggers="hover" placement="top">
                      <template v-slot:title>{{curriculummediumscale}} 학습 진행 사항</template>
                      <p
                        v-if="st.inglist.length > 0"
                        style="margin-bottom: 10px"
                      >--- 진행중({{st.inglist.length}}) ---</p>
                      <p v-for="s in st.inglist" :key="s" style="margin: 0; padding:0">{{ s }}</p>
                      <p
                        v-if="st.donelist.length > 0"
                        style="margin: 10px 0 10px 0"
                      >--- 완료({{st.donelist.length}}) ---</p>
                      <p
                        v-for="s in st.donelist"
                        :key="s"
                        style="margin: 0; padding:0"
                        :class="{purple : s == curriculumsmallscale}"
                      >{{ s }}</p>
                    </b-popover>
                  </div>
                </li>
              </ul>
            </div>
          </li>
          <li>
            <div class="student" style="position: relative" @click="toggle(2)">
              현재 강의 학습 완료
              <p style="width: 25px"></p>
              <img
                v-if="!this.isTwo"
                src="@/assets/img/pp.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
              <img
                v-if="this.isTwo"
                src="@/assets/img/pp2.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
            </div>
            <div class="list" v-if="this.isTwo">
              <ul style="padding-left:0">
                <li v-for="st in student" :key="st.name" style="padding-left:0">
                  <div v-if="st.rank == 2" :id="st.name" style="text-align:center">
                    <p>{{st.name}}</p>
                    <b-popover :target="st.name" triggers="hover" placement="top">
                      <template v-slot:title>{{curriculummediumscale}} 학습 진행 사항</template>
                      <p
                        v-if="st.inglist.length > 0"
                        style="margin-bottom: 10px"
                      >--- 진행중({{st.inglist.length}}) ---</p>
                      <p v-for="s in st.inglist" :key="s" style="margin: 0; padding:0">{{ s }}</p>
                      <p
                        v-if="st.donelist.length > 0"
                        style="margin: 10px 0 10px 0"
                      >--- 완료({{st.donelist.length}}) ---</p>
                      <p
                        v-for="s in st.donelist"
                        :key="s"
                        style="margin: 0; padding:0"
                        :class="{purple : s == curriculumsmallscale}"
                      >{{ s }}</p>
                    </b-popover>
                  </div>
                </li>
              </ul>
            </div>
          </li>
          <li>
            <div class="student" style="position: relative" @click="toggle(3)">
              현재 강의 학습 중
              <span style="width: 23px"></span>
              <img
                v-if="!this.isThree"
                src="@/assets/img/pp.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
              <img
                v-if="this.isThree"
                src="@/assets/img/pp2.png"
                height="15px"
                style="position:absolute; right: 10px"
              />
            </div>
            <div class="list" v-if="this.isThree">
              <ul style="padding-left:0">
                <li v-for="st in student" :key="st.name" style="padding-left:0">
                  <div v-if="st.rank == 3" :id="st.name" style="text-align:center">
                    <p>{{st.name}}</p>
                    <b-popover :target="st.name" triggers="hover" placement="top">
                      <template v-slot:title>{{curriculummediumscale}} 학습 진행 사항</template>
                      <p
                        v-if="st.inglist.length > 0"
                        style="margin-bottom: 10px"
                      >--- 진행중({{st.inglist.length}}) ---</p>
                      <p
                        v-for="s in st.inglist"
                        :key="s"
                        style="margin: 0; padding:0"
                        :class="{purple : s == curriculumsmallscale}"
                      >{{ s }}</p>
                      <p
                        v-if="st.donelist.length > 0"
                        style="margin: 10px 0 10px 0"
                      >--- 완료({{st.donelist.length}}) ---</p>
                      <p
                        v-for="s in st.donelist"
                        :key="s"
                        style="margin: 0; padding:0"
                        :class="{purple : s == curriculumsmallscale}"
                      >{{ s }}</p>
                    </b-popover>
                  </div>
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </b-col>
    </b-row>
  </div>
</template>
<script>
import axios from "axios";
import { EmbedPlugin } from "bootstrap-vue";

export default {
  name: "curriculum",
  components: {},
  data: () => {
    return {
      curriculumlargescale: "",
      curriculummediumscale: "",
      curriculumsmallscale: "",
      link: "",
      contents: "",
      nodeId: "",
      curFinish: "",
      scaleList: "",
      student: [],
      isOne: false,
      isTwo: false,
      isThree: false,
      isFour: false,
    };
  },
  watch: {},
  created() {
    this.nodeId = this.$attrs.nodeId;
    this.getcurriculum();
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
    toggle(data) {
      switch (data) {
        case 1:
          this.isOne = !this.isOne;
          break;
        case 2:
          this.isTwo = !this.isTwo;
          break;
        case 3:
          this.isThree = !this.isThree;
          break;
        case 4:
          this.isFour = !this.isFour;
          break;
      }
    },

    getmycurriculum() {
      axios
        .put(process.env.VUE_APP_API_URL + "getmycurriculum/", {
          useremail: this.$store.state.useremail,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            data.object.forEach((element) => {
              if (
                element.curriculumlargescale == this.curriculumlargescale &&
                element.curriculummediumscale == this.curriculummediumscale &&
                element.curriculumsmallscale == this.curriculumsmallscale
              ) {
                this.curFinish = element.isfinish;
              }
            });
            if (!this.curFinish) this.curFinish = "none";
            this.getStudent();
          } else {
            this.$emit("close");
          }
        });
    },
    //nodeID로 커리큘럼 분류, 유튜브 가져오기
    getcurriculum() {
      axios
        .post(process.env.VUE_APP_API_URL + "getcurriculum/", {
          nodeid: this.nodeId,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.curriculumlargescale = data.object.curriculumlargescale;
            this.curriculummediumscale = data.object.curriculummediumscale;
            this.curriculumsmallscale = data.object.curriculumsmallscale;
            this.link = data.object.link;
            this.contents = data.object.contents;
            this.getmycurriculum();
          } else {
            this.$emit("close");
          }
        });
    },
    //학습참가 버튼 함수
    study(state) {
      if (!this.$store.state.useremail) {
        this.toast("로그인 후 이용가능한 서비스입니다.");
        return;
      }
      axios
        .post(process.env.VUE_APP_API_URL + "student/", {
          curriculumlargescale: this.curriculumlargescale,
          curriculummediumscale: this.curriculummediumscale,
          curriculumsmallscale: this.curriculumsmallscale,
          isfinish: state,
          useremail: this.$store.state.useremail,
          username: this.$store.state.username,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            if (state == "ing") {
              this.toast("학습에 참여했습니다.");
            } else if (state == "done") {
              this.toast("학습을 완료했습니다. 축하합니다!");
            }
            this.curFinish = state;
            this.$emit("update", this.nodeId, this.curFinish);
          } else {
          }
        });
    },
    cancel() {
      axios
        .delete(process.env.VUE_APP_API_URL + "student", {
          params: {
            curriculumlargescale: this.curriculumlargescale,
            curriculummediumscale: this.curriculummediumscale,
            curriculumsmallscale: this.curriculumsmallscale,
            useremail: this.$store.state.useremail,
          },
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.toast("학습 취소");
            this.curFinish = "none";
            this.$emit("update", this.nodeId, this.curFinish);
          } else {
          }
        });
    },
    //함께 스터디중인 멤버 가져오기
    getStudent() {
      axios
        .get(
          process.env.VUE_APP_API_URL +
            "studentsheet/" +
            this.curriculumlargescale +
            "/" +
            this.curriculummediumscale
        )
        .then(({ data }) => {
          if (data.data == "success") {
            this.scaleList = data.object.curriculumsmallscale;
            for (let i = 0; i < data.object.studentName.length; i++) {
              if (this.$store.state.username == data.object.studentName[i])
                continue;
              let s = {};
              s.name = data.object.studentName[i];
              s.cnt = 0;
              s.inglist = [];
              s.donelist = [];
              s.rank = 4;
              for (
                let j = 0;
                j < data.object.curriculumsmallscale.length;
                j++
              ) {
                if (data.object.studentsheet[j][i] == "ing") {
                  s.inglist.push(data.object.curriculumsmallscale[j]);
                  // 진행중이므로 순위 3번째
                  if (
                    data.object.curriculumsmallscale[j] ==
                    this.curriculumsmallscale
                  ) {
                    s.rank = 3;
                  }
                } else if (data.object.studentsheet[j][i] == "done") {
                  s.cnt++;
                  s.donelist.push(data.object.curriculumsmallscale[j]);
                  // 완수 했으므로 순위 2번째
                  if (
                    data.object.curriculumsmallscale[j] ==
                    this.curriculumsmallscale
                  ) {
                    s.rank = 2;
                  }
                }
              }
              if (s.cnt == data.object.curriculumsmallscale.length) s.rank = 1;
              this.student.push(s);
            }
            // console.log(this.student);
            // this.student = data.object;
            // console.log("성공");
          } else {
            // console.log(data);
          }
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.left,
.right {
  margin-top: 30px;
}
ul {
  list-style: none;
}
.student {
  padding: 5px;
  margin-bottom: 20px;
  text-align: center;
  background-color: #dbd3f5;
  justify-content: center;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.list div {
  margin-bottom: 10px;
}
.no-hover {
  cursor: default;
}
.no-hover:hover {
  background: #a593e0;
}
.purple {
  background-color: #a593e0;
  color: white;
}
</style>
