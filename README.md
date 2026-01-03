# Nebula
My Personal Project for fun

# 项目模块

script  - 本项目重要的脚本，包括启动、停止、打包等
  build.sh  - 将所有模块都打包成镜像
  start.sh  - 以mvn方式启动所有模块的dev环境
  stop.sh   - 停止所有模块
  docker-compose.sh  - 使用docker-compose启动所有模块，docker-compose up -d，注意，镜像版本写在.env文件中
  rm-container.sh  - 删除所有容器


ui      - 管理后台前端项目
server  - 管理后台后端项目
  common  - 公共类
  gateway - 网关，dev环境时，8080端口
  auth    - 认证模块，dev环境时候，8081端口
