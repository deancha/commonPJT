import { Line, mixins } from "vue-chartjs";
const { reactiveProp } = mixins;

export default {
  extends: Line,
  mixins: [reactiveProp],
  mounted() {
    this.renderChart(this.chartData, {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        xAxes: [
          {
            ticks: {
              callback: function(value) {
                return value.length > 3 ? value.substring(0, 3) + "..." : value;
              },
            },
          },
        ],
        yAxes: [
          {
            ticks: {
              min: 0,
            },
          },
        ],
      },
    });
  },
};
