var express = require('express');
var router = express.Router();


// 引入 ws 模块
const WebSocket = require('ws').Server;
const port = 3001;


// 创建服务
const server = new WebSocket({ port }, () => {
  console.log('websocket 服务开启')
})

const connectHandler = (ws) => {
  console.log('客户端链接');
  // 监听客户端出错
  ws.on('error', errorHandler)
  // 监听客户端断开链接
  ws.on('close', closeHandler)
  // 监听客户端发来消息
  ws.on('message', messageHandler)
}

const errorHandler = (error) => {
  console.log('errorHandler==> 客户端出错', error)
}

const closeHandler = (e) => {
  console.log('closeHandler==> 客户端断开链接', e)
}

function messageHandler (data) {
  console.log('messageHandler==> 客户端发来消息', JSON.parse(data))

  const { ModeCode } = JSON.parse(data);

  switch (ModeCode) {
    case 'message':
      console.log('收到普通消息')
      break;
    case 'heart_beat':
      console.log('心跳检测')
      this.send(JSON.stringify(JSON.parse(data)));
      break;
  }
}




// 建立链接
server.on('connection', connectHandler);


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
