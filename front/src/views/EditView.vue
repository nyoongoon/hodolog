<script setup lang="ts">
import {defineProps, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

//초기화 데이터 넣기
const post = ref({
  id: 0,
  title: "",
  content: ""
})

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then((response) => {
    router.replace({name: "home"});
  });
}

</script>

<template>
  <div>
    <el-input v-model="post.title" type="text"/>
  </div>
  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"/>
  </div>
  <div class="mt-2 d-flex justify-content-end">
    <el-button type="primary" @click="edit()">글 수정 완료</el-button>
  </div>


</template>

<style scoped>

</style>