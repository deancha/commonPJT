<template>
  <div class="post" style="max-width: 99%">
    <h1 style="text-align: center; margin: 10px 0 20px 0;font-size: 30pt;">통계 페이지</h1>
    <div class="content">
      <div id="word-cloud" style="height: 400px"></div>

      <b-row>
        <b-col md="6">
          <b-card title="조회수 순" class="main-card mb-3">
            <bar v-if="isView" :chart-data="view"></bar>
          </b-card>
        </b-col>
        <b-col md="6">
          <b-card title="좋아요 순" class="main-card mb-3">
            <lineeg v-if="isLike" :chart-data="like"></lineeg>
          </b-card>
        </b-col>
      </b-row>
      <b-row>
        <b-col md="12">
          <b-card title="방문자" class="main-card mb-3">
            <linevisitor v-if="isVisitor" :chart-data="visitor"></linevisitor>
          </b-card>
        </b-col>
      </b-row>
    </div>
  </div>
</template>

<script>
import Anychart from "anychart";
import linevisitor from "./Chartjs/LineVisitor";
import lineeg from "./Chartjs/Line";
import bar from "./Chartjs/Bar";
import barhoriz from "./Chartjs/BarHoriz";
import axios from "axios";

export default {
  components: {
    lineeg,
    bar,
    barhoriz,
    Anychart,
    linevisitor,
  },
  data: () => ({
    isView: false,
    isLike: false,
    isVisitor: false,
    view: {
      labels: null,
      datasets: null,
    },
    like: {
      labels: null,
      datasets: null,
    },
    visitor: {
      datasets: [
        {
          label: "방문자 수",
          fill: false,
          lineTension: 0.1,
          backgroundColor: "#ed0f51",
          borderColor: "#ed0f51",
          borderCapStyle: "round",
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: "miter",
          pointBorderColor: "#ed0f51",
          pointBackgroundColor: "#ffffff",
          pointBorderWidth: 2,
          pointHoverRadius: 10,
          pointHoverBackgroundColor: "#ed0f51",
          pointHoverBorderColor: "#ed0f51",
          pointHoverBorderWidth: 2,
          pointRadius: 3,
          pointHitRadius: 10,
          data: [],
        },
      ],
      labels: [],
    },
    chart: null,
    isAny: false,
  }),
  created() {
    this.getCountView();
    this.getLikeView();
    this.getCountVisitor();
  },
  mounted() {
    if (!this.chart) this.init();
  },
  methods: {
    init() {
      let _Anychart = this.Anychart || Anychart;
      var wordData = null;
      axios
        .get(process.env.VUE_APP_API_URL + "getwordcloud")
        .then(({ data }) => {
          if (data.data == "success") {
            wordData = data.object;
            this.chart = new _Anychart.tagCloud(wordData);
            this.chart.title("멋쟁이 상원처럼 WordCloud");
            this.chart.angles([0]);
            this.chart.colorRange(false);
            this.chart.colorRange().length("80%");
            this.chart.container("word-cloud").draw();
          }
        });
    },
    getCountView() {
      axios
        .get(process.env.VUE_APP_API_URL + "getHotpostforWeek")
        .then(({ data }) => {
          if (data.data == "success") {
            this.view.datasets = [data.object.statisticviewWrapperclass];
            this.view.labels = data.object.labels;
            this.isView = true;
          }
        });
    },
    getLikeView() {
      axios
        .get(process.env.VUE_APP_API_URL + "getMostLikeforWeek")
        .then(({ data }) => {
          if (data.data == "success") {
            this.like.datasets = [data.object.statisticviewWrapperclass];
            this.like.labels = data.object.labels;
            this.isLike = true;
          }
        });
    },
    getCountVisitor() {
      axios.get(process.env.VUE_APP_API_URL + "visitorall").then(({ data }) => {
        if (data.data == "success") {
          if (data.object.length > 10) {
            for (var i = data.object.length - 10; i < data.object.length; i++) {
              this.visitor.datasets[0].data.push(data.object[i].visitors);
              this.visitor.labels.push(data.object[i].date.substring(5, 10));
            }
          } else {
            data.object.forEach((element) => {
              this.visitor.datasets[0].data.push(element.visitors);
              this.visitor.labels.push(element.date.substring(5, 10));
            });
          }
          this.isVisitor = true;
        }
      });
    },
  },
};
</script>
<style>
.main-title * {
  max-width: 600px;
}
#word-cloud {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>