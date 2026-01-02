# Nebula Server - 停止服务脚本

## stop.sh 使用说明

### 功能
关闭所有在 script 目录下运行的Spring Boot服务。

### 使用方法

在 script 目录下执行：
```bash
./stop.sh
```

### 停止的服务

脚本会自动停止以下服务：
1. **Gateway Service** (端口: 8080)
2. **Auth Service** (端口: 8081)
3. 任何其他Maven Spring Boot进程

### 工作原理

1. **优雅关闭**：首先尝试使用`kill`命令优雅地关闭服务
2. **等待时间**：最多等待10秒让服务完成清理工作
3. **强制关闭**：如果服务在10秒内没有停止，使用`kill -9`强制关闭
4. **清理进程**：检查并关闭所有剩余的Maven Spring Boot进程

### 输出示例

```
========================================
  Stopping Nebula Server Services
========================================

Stopping services by port...

Stopping Gateway Service (PID: 83733, Port: 8080)...
✓ Gateway Service stopped successfully
Stopping Auth Service (PID: 83673, Port: 8081)...
✓ Auth Service stopped successfully

Checking for remaining Spring Boot processes...
✓ No remaining Maven processes found

========================================
  All services stopped
========================================
```

### 颜色标识

- 🟢 **绿色**：成功操作
- 🟡 **黄色**：警告或进行中
- 🔴 **红色**：错误或失败

### 注意事项

1. 确保在 server 目录下执行脚本
2. 需要执行权限（已设置：`chmod +x stop.sh`）
3. 脚本会自动检测服务是否正在运行
4. 即使某些服务未运行，脚本也会安全地完成执行

### 相关命令

**手动检查端口占用：**
```bash
lsof -i:8080  # 检查Gateway端口
lsof -i:8081  # 检查Auth端口
```

**手动强制停止所有Spring Boot进程：**
```bash
pkill -9 -f "spring-boot:run"
```

**查看所有Java进程：**
```bash
ps aux | grep java
```
