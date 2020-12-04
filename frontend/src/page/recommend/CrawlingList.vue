<template>
      <div> 
        <b-card-group class="group" columns>
          <b-card 
            v-for="post in listArray"
            :key="post.url"> 
            <a :href="post.url">
                <b-card-title class="dohyeon ">{{ post.title }}</b-card-title>
                <b-card-text class="dohyeon author">
                {{ post.author }}
                </b-card-text>
            </a>
          </b-card>
        </b-card-group>
      </div>
</template>

<script>

export default {
    data() {
        return {
        }
    },
    props: {
    listArray: {
      type: Array,
      required: true,
    },
    },
};
</script>
<style scoped>

.card {
  box-shadow: 8px 8px 2px 1px rgba(0, 0, 255, .2);
          

}
.card :hover {
  transform: scale(1.02);
    transition: 0.2s all;   
}


.author {
     color: #a593e0;
}

.card a {
    text-decoration: none;
}
</style>