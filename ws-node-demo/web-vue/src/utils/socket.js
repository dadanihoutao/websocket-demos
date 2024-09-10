class Socket {
  wsUrl
  constructor (wsUrl) {
    this.wsUrl = wsUrl
  }
  ModeCode = {
    MSG: 'message', // 普通消息
    HEART_BEAT: 'heart_beat', // 心跳检测消息
  }

  ws = null
  webSocketState = false // ws 链接状态
  heartBeat = {
    time: 5 * 1000, // 心跳检测间隔时间
    timeout: 3 * 1000, // 心跳检测超时时间
    reconnect: 5 * 1000, // 断线重连时间
  }
  reconnectTimer = null // 断线重连时间器


  connectWebSocket () {
    this.ws = new WebSocket(this.wsUrl);
    this.init()
  }

  // 初始化
  init () {
    this.ws.addEventListener('open', () => {
      // ws 链接状态改成 true
      this.webSocketState = true
      // 是否开启心跳检测
      this.heartBeat?.time && this.startHeartBeat(this.heartBeat.time)
      console.log("开启");

      this.ws.addEventListener('message', (e) => {
        console.log(e.data)
        const data = JSON.parse(e.data);
        switch (data.ModeCode) {
          case this.ModeCode.MSG:
            console.log('普通消息')
            break
          case this.ModeCode.HEART_BEAT:
            this.webSocketState = true
            console.log('心跳消息', data.msg)
            break
        }
      })

      this.ws.addEventListener('close', (e) => {
        this.webSocketState = false;
        console.log('断开了链接', e)
      })

      this.ws.addEventListener('error', (e) => {
        this.webSocketState = false
        this.reconnectWebSocket(); // 重连
        console.log('链接发生错误', e)
      })

    })
  }

  // 心跳检测初始化函数
  startHeartBeat (time) {
    setTimeout(() => {
      this.ws.send(
        JSON.stringify({
          ModeCode: this.ModeCode.HEART_BEAT,
          msg: new Date()
        })
      )
      this.waitingServer();
    }, time)
  }

  // 演示等待服务端相应，通过 webSocketState 判断是否连线成功
  waitingServer () {
    this.webSocketState = false;
    setTimeout(() => {
      if (this.webSocketState) {
        this.startHeartBeat(this.heartBeat.time)
        return
      }
      console.log('心跳无响应，已经断线了')
      try {
        this.ws.close()
      } catch (e) {
        console.log('链接已关闭')
      }
      this.reconnectWebSocket()
    }, this.heartBeat.timeout)
  }

  // 重新链接
  reconnectWebSocket () {
    this.reconnectTimer = setTimeout(() => {
      console.log('准备重新链接')
      this.reconnectWs();
    }, this.heartBeat.reconnect);
  }

  reconnectWs () {
    if (!this.ws) {
      // 第一次执行，初始化
      this.connectWebSocket()
    } else if (this.ws && this.reconnectTimer) {
      // 防止多个 WebSocket 同时执行
      clearTimeout(this.reconnectTimer);
      this.ws.reconnectTimer = null;
      this.ws = null;
      this.reconnectWebSocket()
    }
  }
}

export default Socket