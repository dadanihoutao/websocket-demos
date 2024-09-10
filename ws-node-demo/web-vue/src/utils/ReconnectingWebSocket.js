// 没有验证过的 ws 类
export default class ReconnectingWebSocket {
  constructor(url, protocols = []) {
    this.url = url;
    this.protocols = protocols;
    this.ws = null;
    this.reconnectDelay = 1000; // 重连间隔（初始值1秒）
    this.maxReconnectDelay = 30000; // 最大重连间隔
    this.isManuallyClosed = false; // 标记是否是手动关闭
    this.heartbeatInterval = 10000; // 心跳间隔（10秒）
    this.heartbeatTimer = null;
    this.isOpen = false; // 是否链接成功
    this.connect();
  }

  connect() {
    this.ws = new WebSocket(this.url, this.protocols);
    
    // 处理 WebSocket 连接打开
    this.ws.onopen = () => {
      console.log('WebSocket 连接成功');
      this.isOpen = true
      this.startHeartbeat(); // 连接成功后启动心跳检测
    };

    // 处理接收到的消息
    this.ws.onmessage = (message) => {
      this.isOpen = true
      console.log('收到消息:', message.data);
      this.resetHeartbeat(); // 每次收到消息时重置心跳
    };

    // 处理 WebSocket 连接关闭
    this.ws.onclose = () => {
      console.log('WebSocket 连接关闭');
      this.isOpen = false
      this.clearHeartbeat(); // 清除心跳
      if (!this.isManuallyClosed) {
        this.reconnect();
      }
    };

    // 处理 WebSocket 错误
    this.ws.onerror = (error) => {
      console.log('WebSocket 错误:', error);
      this.isOpen = false
      this.ws.close(); // 遇到错误时关闭连接
    };
  }

  reconnect() {
    // 逐渐增加重连时间，最多到 maxReconnectDelay
    let delay = Math.min(this.reconnectDelay, this.maxReconnectDelay);
    console.log(`正在重连，${delay / 1000}秒后尝试重连...`);
    
    setTimeout(() => {
      console.log('尝试重新连接...');
      this.connect();
      this.reconnectDelay *= 2; // 每次重连失败后增加间隔时间
    }, delay);
  }

  send(message) {
    if (this.isOpen) {
      this.ws.send(JSON.stringify(message));
    } else {
      console.log('WebSocket 未连接，无法发送消息');
    }
  }

  close() {
    this.isManuallyClosed = true; // 标记手动关闭
    this.clearHeartbeat();
    this.ws.close(); // 手动关闭连接
  }

  // 心跳检测：定时发送心跳包
  startHeartbeat() {
    this.clearHeartbeat();
    this.heartbeatTimer = setInterval(() => {
      if (this.ws.readyState === WebSocket.OPEN) {
        console.log('发送心跳包');
        this.ws.send('heartbeat');
      }
    }, this.heartbeatInterval);
  }

  resetHeartbeat() {
    this.clearHeartbeat();
    this.startHeartbeat(); // 重置心跳机制
  }

  clearHeartbeat() {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }
}
