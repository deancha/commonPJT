<template>
  <div class="container">
    <b-tabs content-class="mt-3" fill>
      <b-tab title="주간 인기 블로그" active>
        <div v-if="!PopularPosts" >
          <PreLoader />
        </div>
        <CrawlingList :listArray="PopularPosts" />
      </b-tab>
      <b-tab title="최신 개인 블로그">
        <CrawlingList :listArray="PersonalBlogPosts" />
      </b-tab>
      <b-tab title="최신 팀 블로그">
        <CrawlingList :listArray="TeamBlogPosts" />
      </b-tab>
      <b-tab title="방송">
        <CrawlingList :listArray="BroadCastPosts" />
      </b-tab>
    </b-tabs>
  </div>
</template>

<script>
import axios from "axios";
import CrawlingList from "./CrawlingList.vue";
import PreLoader from "./PreLoader.vue";


export default {
  name: "Recommend",
  components: {
    CrawlingList,
    PreLoader
  },
  data: () => {
    return {
      PopularPosts: null,
      PersonalBlogPosts: null,
      TeamBlogPosts: null,
      BroadCastPosts: null,
    };
  },
  methods: {
    CrawlPosts() {
      axios.get(process.env.VUE_APP_API_URL + "crawling").then(({ data }) => {
        if (data.data == "success") {
          //   console.log("data", data);
          //   console.log("data object cl ", data.object[1]["crawlingList"]);
          this.PopularPosts = data.object[0]["crawlingList"];
          this.PersonalBlogPosts = data.object[1]["crawlingList"];
          this.TeamBlogPosts = data.object[2]["crawlingList"];
          this.BroadCastPosts = data.object[3]["crawlingList"];
        } else {
          // console.log(data);
        }
      });
    },
  },
  created() {
    this.CrawlPosts();
  },
};
</script>

<style scoped>
.content {
  padding: 0;
  overflow: hidden;
  background-color: #e7e7e7;
  background-color: rgba(0, 0, 0, 0.06);
}

h1 {
  padding-bottom: 15px;
  border-bottom: 1px solid #d8d8d8;
  font-weight: 600;
}

h1 span {
  font-family: monospace, serif;
}

h3 {
  padding-bottom: 20px;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.1), 0 2px 0 rgba(255, 255, 255, 0.9);
}

p {
  margin: 0;
  padding: 10px 0;
  color: #777;
}

.clear {
  clear: both;
}



</style>