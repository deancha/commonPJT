<template>
  <div>
    <h3>{{ $store.state.username }}님이 작성한 댓글 입니다</h3>
    <PaginatedCommentList />
  </div>
</template>

<script>
import axios from "axios";
import PaginatedCommentList from "./PaginatedCommentList";

export default {
  name: "comment-pagination",
  components: {
    PaginatedCommentList,
  },
};
</script>

<style scoped>
h3 {
  color: #454545;
  text-align: center;
  align-items: center;
  justify-content: center;
  display: flex;
  margin-bottom: 30px;
}
</style>

