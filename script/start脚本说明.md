# Nebula Server - 脚本使用说明

## 📋 概述

`script/` 目录包含了用于管理Nebula Server服务的Shell脚本。

## 🚀 start.sh - 启动所有服务

### 功能
自动启动所有Spring Boot微服务模块。

### 启动的服务
1. **Auth Service** (端口: 8081)
2. **Gateway Service** (端口: 8080)

### 使用方法

```bash
cd /home/sherry/workspace/Nebula/script
./start.sh
```

### 工作流程

1. **环境检查**
   - 检查Maven是否已安装
   - 验证server目录是否存在

2. **端口检查**
   - 检查目标端口是否已被占用
   - 如果服务已运行，跳过启动

3. **服务启动**（按顺序）
   - 首先启动Auth Service
   - 等待Auth Service完全启动（最多60秒）
   - 然后启动Gateway Service
   - 等待Gateway Service完全启动（最多60秒）

4. **启动方式**
   - 使用`mvn spring-boot:run`命令
   - 在后台运行（nohup）
   - 日志输出到`logs/`目录

5. **PID管理**
   - 每个服务的PID保存在`logs/${module}.pid`
   - 便于后续管理和监控

### 输出示例

```
========================================
  Starting Nebula Server Services
========================================

Checking prerequisites...
✓ Maven is installed
✓ Server directory: /home/sherry/workspace/Nebula/server

Starting services in order:
  1. Auth Service (dependency for others)
  2. Gateway Service (entry point)

----------------------------------------
Starting Auth Service
----------------------------------------
Starting Auth Service in background...
  Module: auth
  Port: 8081
  Log: /home/sherry/workspace/Nebula/script/logs/auth.log
✓ Auth Service started with PID: 85999
Waiting for Auth Service to start (port: 8081)....
✓ Auth Service is now running on port 8081

----------------------------------------
Starting Gateway Service
----------------------------------------
Starting Gateway Service in background...
  Module: gateway
  Port: 8080
  Log: /home/sherry/workspace/Nebula/script/logs/gateway.log
✓ Gateway Service started with PID: 86182
Waiting for Gateway Service to start (port: 8080).....
✓ Gateway Service is now running on port 8080

========================================
  All Services Started Successfully
========================================

Service Status:
  - Auth Service:  http://localhost:8081
  - Gateway:       http://localhost:8080

API Endpoints:
  - Health (Auth):     http://localhost:8081/actuator/health
  - Health (Gateway):  http://localhost:8080/actuator/health
  - Hello (Direct):    http://localhost:8081/hello
  - Hello (Gateway):   http://localhost:8080/api/auth/hello

Log files: /home/sherry/workspace/Nebula/script/logs/
To stop all services, run: ./stop.sh
```

### 日志文件

启动后，日志文件保存在`script/logs/`目录：
- `auth.log` - Auth Service日志
- `gateway.log` - Gateway Service日志
- `auth.pid` - Auth Service进程ID
- `gateway.pid` - Gateway Service进程ID

查看日志：
```bash
# 查看实时日志
tail -f logs/auth.log
tail -f logs/gateway.log

# 查看最近的日志
tail -100 logs/auth.log
tail -100 logs/gateway.log
```

## 🛑 stop.sh - 停止所有服务

### 功能
优雅地停止所有正在运行的Spring Boot服务。

### 使用方法

```bash
cd /home/sherry/workspace/Nebula/script
./stop.sh
```

### 工作流程

1. **按端口停止服务**
   - 通过端口号查找进程
   - 发送SIGTERM信号优雅关闭
   - 等待最多10秒让服务完成清理

2. **强制关闭**
   -如果服务在10秒内未停止，发送SIGKILL强制关闭

3. **清理剩余进程**
   - 检查并关闭所有Maven Spring Boot进程
   - 确保没有残留进程

### 输出示例

```
========================================
  Stopping Nebula Server Services
========================================

Stopping services by port...

Stopping Gateway Service (PID: 86282, Port: 8080)...
✓ Gateway Service stopped successfully
Stopping Auth Service (PID: 86107, Port: 8081)...
✓ Auth Service stopped successfully

Checking for remaining Spring Boot processes...
✓ No remaining Maven processes found

========================================
  All services stopped
========================================
```

## 🔧 颜色标识

- 🟢 **绿色**：成功操作
- 🟡 **黄色**：警告或进行中
- 🔴 **红色**：错误或失败
- 🔵 **蓝色**：信息提示

## 📝 注意事项

### 1. 执行位置
**重要**：必须在`script`目录下执行脚本

```bash
cd /home/sherry/workspace/Nebula/script
./start.sh  # ✅ 正确
./stop.sh   # ✅ 正确

# 错误示例
~/script/start.sh  # ❌ 可能导致路径问题
```

### 2. 执行权限
脚本已设置执行权限，如果遇到权限问题：

```bash
chmod +x script/start.sh
chmod +x script/stop.sh
```

### 3. Java和Maven版本
确保安装了正确的版本：
- Java: 21+
- Maven: 3.6+

检查版本：
```bash
java -version
mvn -version
```

### 4. 端口占用
如果端口被占用，start.sh会跳过已运行的服务。

手动检查端口：
```bash
lsof -i:8080  # Gateway端口
lsof -i:8081  # Auth端口
```

### 5. 服务启动顺序
脚本会按照正确的顺序启动服务：
1. 先启动Auth Service（业务服务）
2. 后启动Gateway Service（入口服务）

这个顺序确保Gateway启动时，Auth服务已经可用。

## 🧪 快速测试

启动服务后，可以快速测试：

```bash
# 测试Auth服务（直接访问）
curl http://localhost:8081/hello

# 测试Gateway路由
curl http://localhost:8080/api/auth/hello

# 测试健康检查
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

## 🔄 常用操作

### 重启所有服务

```bash
cd /home/sherry/workspace/Nebula/script
./stop.sh
sleep 3
./start.sh
```

### 只重启某个服务

```bash
# 找到进程ID
cat logs/auth.pid

# 停止服务
kill <PID>

# 启动服务（手动）
cd ../server/auth
mvn spring-boot:run
```

### 查看服务状态

```bash
# 检查端口是否监听
netstat -tlnp | grep -E '8080|8081'

# 或者使用lsof
lsof -i:8080
lsof -i:8081

# 查看进程
ps aux | grep spring-boot:run
```

### 清理日志文件

```bash
cd /home/sherry/workspace/Nebula/script
rm -rf logs/*
```

## 📂 目录结构

```
Nebula/
├── script/
│   ├── start.sh              # 启动脚本
│   ├── stop.sh               # 停止脚本
│   ├── build.sh              # 构建脚本（待实现）
│   ├── logs/                 # 日志目录（自动创建）
│   │   ├── auth.log         # Auth服务日志
│   │   ├── gateway.log      # Gateway服务日志
│   │   ├── auth.pid         # Auth进程ID
│   │   └── gateway.pid      # Gateway进程ID
│   └── dockerfile/           # Docker相关文件
└── server/                   # 后端服务目录
    ├── common/              # 公共模块
    ├── auth/                # 认证服务
    └── gateway/             # 网关服务
```

## 🐛 故障排除

### 问题1：服务启动失败

**症状**：启动超时或端口未监听

**解决**：
1. 检查日志文件：`tail -100 logs/auth.log`
2. 确认端口未被占用：`lsof -i:8081`
3. 检查Maven配置：`mvn -version`
4. 手动启动测试：`cd ../server/auth && mvn spring-boot:run`

### 问题2：服务无法停止

**症状**：stop.sh执行后端口仍被占用

**解决**：
```bash
# 强制杀死进程
pkill -9 -f "spring-boot:run"

# 或者通过端口杀死进程
lsof -ti:8080 | xargs kill -9
lsof -ti:8081 | xargs kill -9
```

### 问题3：权限被拒绝

**症状**：`Permission denied`

**解决**：
```bash
chmod +x script/start.sh
chmod +x script/stop.sh
```

### 问题4：Maven命令找不到

**症状**：`mvn: command not found`

**解决**：
1. 确认Maven已安装：`which mvn`
2. 检查PATH环境变量
3. 安装Maven（如果未安装）

## 📚 相关文档

- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Maven官方文档](https://maven.apache.org/guides/)
- [项目README](../README.md)
