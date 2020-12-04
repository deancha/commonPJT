<template>
  <div>
    <h3>{{ $store.state.username }}님이 좋아요를 누른 포스트 입니다</h3>
    <paginated-like-list />
  </div>
</template>

<script>
import PaginatedLikeList from "./PaginatedLikeList";

export default {
  name: "TableLikePagination",
  components: {
    PaginatedLikeList,
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
