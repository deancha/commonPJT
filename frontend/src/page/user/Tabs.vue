<template>
  <article>
    <header class="tabs">
      <ul>
        <button
          :class="{ 'is-active': tab.isActive }"
          v-for="(tab, index) in tabs"
          :key="index"
          @click="selectTab(tab)"
        >{{ tab.name }}</button>
      </ul>
    </header>
    <section class="tabs-details">
      <slot></slot>
    </section>
  </article>
</template>

<script>
export default {
  data: () => {
    return {
      tabs: [],
    };
  },
  methods: {
    selectTab(selectedTab) {
      this.tabs.forEach((tab) => {
        tab.isActive = tab.name === selectedTab.name;
      });
    },
  },
  created() {
    this.tabs = this.$children;
  },
};
</script>
<style lang="scss" scoped>
.tabs {
  border-bottom: 1px solid #fefefe;
  margin: 0 10px;
}

.tabs-details {
  padding: 10px;
}

ul {
  display: flex;
  padding: 0;
  list-style: none;

  li {
    margin-right: 40px;
  }

  button {
    color: black;
    &:hover {
      background-color: #a593e0;
      color: white;
    }
    &.is-active {
      background-color: #a593e0;
      color: white;
    }
  }

  // .nav-item {
  //   cursor: pointer;
  //   color: #a593e0;

  //   &:hover {
  //     color: #fefefe;
  //   }

  //   &.is-active {
  //     color: #fefefe;
  //   }
  // }
}
</style>
