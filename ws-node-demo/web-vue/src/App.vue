<script setup>
  import { onMounted } from 'vue';
  import Socket from './utils/socket'

  const socket = new Socket('ws://localhost:3001');


  onMounted(() => {
    socket.connectWebSocket()
  })


  // // 1实例化
  // const ws = new WebSocket('ws://localhost:3001');

  // // 2. open
  // ws.onopen = function () {
  //   console.log('连接成功');
  //   ws.send('123123132');
  // }

  // // 3. message
  // ws.onmessage = function (e) {
  //   console.log(e.data);
  // }

  // // 4. close
  // ws.onclose = function () {
  //   console.log('连接关闭');
  // }

  // // 5. error
  // ws.onerror = function () {
  //   console.log('连接错误');
  // }

</script>

<template>
  <div>123123</div>
</template>

<style scoped>
</style>
