<script setup lang="ts">

</script>

<template>
  <el-header class="p-0">
    <el-menu mode="horizontal" :router="true">
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">글 작성</el-menu-item>
    </el-menu>
  </el-header>
</template>

<style scoped>
body {
  magin: 0;
}
</style>