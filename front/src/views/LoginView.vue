<script setup lang="ts">
import { reactive } from 'vue'
import Login from '@/entity/user/Login'
import axios from 'axios'

// 클래스 데이터를 사용
const state = reactive({
  login: new Login()
})

function doLogin() {
  console.log('>>', state.login)
  axios.post('/api/auth/login', state.login)
}
</script>

<template>
  <el-row>
    <el-col :span="10" :offset="7">
      <el-form label-position="top">
        <el-form-item label="이메일">
          <el-input v-model="state.login.email"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.login.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doLogin()">로그인</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>
