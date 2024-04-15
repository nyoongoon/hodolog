<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const posts = ref([]);
const router = useRouter();

axios.get("/api/posts?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  })
});

const moveToRead = () => {
  router.push({name: "read"});
}

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id" @click="moveToRead()">
      <div>
        {{ post.title }}
      </div>
      <div>
        {{ post.content }}
      </div>
    </li>
  </ul>
</template>
