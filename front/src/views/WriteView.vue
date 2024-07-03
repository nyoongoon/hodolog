<script setup lang="ts"></script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input size="large" placeholder="제목을 입력해주세요" />
    </el-form-item>

    <el-form-item label="내용">
      <el-input type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%">등록완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style></style>
