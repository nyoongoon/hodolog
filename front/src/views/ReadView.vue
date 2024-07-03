<script setup lang="ts"></script>

<template>
  <el-row>
    <el-col :span="22" :offset="1">
      <div class="title"></div>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="10" :offset="7">
      <div class="title">
        <div class="regDate"></div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="content"></div>

      <div class="footer">
        <div class="edit">수정</div>
        <div class="delete">삭제</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="comments">
    <el-col>
      <Comments />
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.8rem;
  font-weight: 400;
  text-align: center;
}

.regDate {
  margin-top: 0.5rem;
  font-size: 0.78rem;
  font-weight: 300;
}

.content {
  margin-top: 1.88rem;
  font-weight: 300;

  word-break: break-all;
  white-space: break-spaces;
  line-height: 1.4;
  min-height: 5rem;
}

hr {
  border-color: #f9f9f9;
  margin: 1.2rem 0;
}

.footer {
  margin-top: 1rem;
  display: flex;
  font-size: 0.78rem;
  justify-content: flex-end;
  gap: 0.8rem;

  .delete {
    color: red;
  }
}

.comments {
  margin-top: 4.8rem;
}
</style>
