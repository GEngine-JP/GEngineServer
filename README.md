
[![Build Status](https://travis-ci.org/GameUnion/GameServer.svg?branch=master)](https://travis-ci.org/GameUnion/GameServer)
[![GitHub issues](https://img.shields.io/github/issues/GameUnion/GameServer.svg)](https://github.com/GameUnion/GameServer/issues)
[![GitHub forks](https://img.shields.io/github/forks/GameUnion/GameServer.svg)](https://github.com/GameUnion/GameServer/network)
[![GitHub stars](https://img.shields.io/github/stars/GameUnion/GameServer.svg)](https://github.com/GameUnion/GameServer/stargazers)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/GameUnion/GameServer/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![GitHub followers](https://img.shields.io/github/followers/houko.svg?style=social&label=Follow)]()
[![GitHub watchers](https://img.shields.io/github/watchers/GameUnion/GameServer.svg?style=social&label=Watch)]()

# 项目环境
- 本项目采用maven多模块构建。
- JDK11
- netty4.1.42

# 项目依赖
- 服务器核心包 [ServerCore](https://github.com/GameUnion/ServerCore)
- 通讯协议 [ConfigProtocol](https://github.com/GameUnion/ConfigProtocol)
- 数值数据 [ConfigData](https://github.com/GameUnion/ConfigData)
- Unity客户端 [GameClient](https://github.com/GameUnion/GameClient)

# 启动方式
- 替换[config.properties](server/src/main/resources/config.properties)文件的configDataPath
- 替换[game.properties](server/src/main/resources/game.properties)文件的数据库配置
- 右键 运行[GameServerBootstrap](server/src/main/java/info/xiaomo/server/GameServerBootstrap.java)


# 鸣谢
[贝密游戏](http://git.oschina.net/beimigame/beimi): 使用贝密游戏的开源UI


# 项目预览

![输入图片说明](screenshot/大厅.png "屏幕截图.png")
![输入图片说明](screenshot/大厅2.png "屏幕截图.png")
![输入图片说明](screenshot/majiang.jpeg "屏幕截图.png")
![输入图片说明](screenshot/分享.png "屏幕截图.png")
![输入图片说明](screenshot/反馈.png "屏幕截图.png")
![输入图片说明](screenshot/房间号.png "屏幕截图.png")
![输入图片说明](screenshot/加入房间.png "屏幕截图.png")
![输入图片说明](screenshot/消息.png "屏幕截图.png")
![输入图片说明](screenshot/设置.png "屏幕截图.png")      
