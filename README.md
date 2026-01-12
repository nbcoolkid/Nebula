# Nebula

个人中后台管理系统，后端是权限开发的，前端是基于art-design-pro的

# 启动项目

## 数据库初始化脚本

TODO

## 启动前端

```bash
cd ui
pnpm install
pnpm dev
```

## 启动后端

- mvn的方式启动后端，用于开发时

```bash
cd script
sh start.sh

停止
sh stop.sh
```

- docker-compose的方式启动后端，用于生产环境

```bash
cd script/docker-compose
docker-compose up -d

停止
docker-compose down
```

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

# 前端开发规范

## 前端核心逻辑

## 前端开发规范

# 后端开发规范

## 1. 命名规范

### 1.1 包名命名
- **规范**: 全小写字母，使用域名反写格式
- **示例**: `com.nebula.auth.controller`、`com.nebula.common.result`

```java
✅ 正确: com.nebula.auth.service.impl
❌ 错误: com.nebula.auth.Service.Impl
```

### 1.2 类名命名
- **规范**: 大驼峰命名法（PascalCase），名词或名词短语

```java
// Controller 层
public class HelloController { }

// Service 层
public class HelloService { }

// Repository 层
public interface UserRepository extends JpaRepository<User, Long> { }

// Entity 层
@Data
@Entity
@Table(name = "user")
public class User { }

// 配置类
public class WebConfig { }

// 数据传输对象
public class Result<T> { }
```

**命名约定**:
- Controller: `{功能名}Controller`
- Service: `{功能名}Service`
- Repository: `{实体名}Repository`（如 `UserRepository`）
- Entity: 与数据库表名对应（如 `user` → `User`，`user_order` → `UserOrder`）
- Config: `{功能名}Config`
- Filter: `{功能名}Filter`

### 1.3 方法名命名
- **规范**: 小驼峰命名法（camelCase），动词开头

```java
// Controller 方法
@GetMapping
public Result<String> hello() { }

@GetMapping("/personal")
public Result<String> helloName(@RequestParam String name) { }

// Service 方法
public String getGreeting() { }
public String getPersonalizedGreeting(String name) { }
public List<User> listUsers() { }
public void saveUser(User user) { }
public void updateUser(User user) { }
public void deleteUser(Long id) { }
```

**常用动词前缀**:
- `get`: 获取单个对象
- `list`: 获取列表
- `count`: 统计数量
- `save`: 保存
- `update`: 更新
- `delete`: 删除
- `is`: 判断布尔值

### 1.4 变量名命名
- **规范**: 小驼峰命名法，见名知意

```java
✅ 正确:
private final HelloService helloService;
private String timestamp;
private DateTimeFormatter formatter;

❌ 错误:
private final HelloService hs;
private String t;
```

### 1.5 常量命名
- **规范**: 全大写字母，单词间用下划线分隔

```java
✅ 正确:
private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
private static final int DEFAULT_PAGE_SIZE = 10;

❌ 错误:
private static final DateTimeFormatter formatter = ...;
private static final int DefaultPageSize = 10;
```

### 1.6 注释规范
- **JavaDoc 注释**: 类、接口、公共方法必须添加

```java
/**
 * Hello API Controller
 * Provides a simple greeting endpoint for testing
 */
@Slf4j
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    /**
     * Simple hello endpoint
     * @return greeting message wrapped in Result
     */
    @GetMapping
    public Result<String> hello() {
        log.info("Hello endpoint called");
        return Result.success("Hello World");
    }
}
```

---

## 2. 代码分层架构

### 2.1 分层结构
```
com.nebula.{module}
├── controller/        # 控制层 - 接收请求、参数验证、返回响应
├── service/           # 服务层 - 业务逻辑
├── repository/        # 持久层 - 数据访问（JPA Repository）
├── entity/           # 实体类（JPA Entity）
├── config/           # 配置类
├── dto/              # 数据传输对象
│   ├── req/         # 请求 DTO
│   └── resp/        # 响应 DTO
├── vo/               # 视图对象
├── enums/            # 枚举类
├── exception/        # 自定义异常
├── util/             # 工具类
└── {Module}Application.java  # 启动类
```

### 2.2 各层职责

**Controller 层**:
- ✅ 接收 HTTP 请求
- ✅ 参数验证（@Valid）
- ✅ 调用 Service 层方法
- ✅ 封装返回结果（Result）
- ❌ 不包含业务逻辑
- ❌ 不直接访问数据库

```java
@Slf4j
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;

    @GetMapping
    public Result<String> hello() {
        log.info("Hello endpoint called");
        String greeting = helloService.getGreeting();
        return Result.success(greeting, "Hello request successful");
    }
}
```

**Service 层**:
- ✅ 业务逻辑处理
- ✅ 事务控制（@Transactional）
- ✅ 调用 Repository 层
- ❌ 不包含 HTTP 相关代码
- ❌ 不直接处理请求/响应

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class HelloService {
    private final HelloRepository helloRepository;
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String getGreeting() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.debug("Generating default greeting at {}", timestamp);
        return String.format("Hello from Nebula Auth Service! Current time: %s", timestamp);
    }

    public String getPersonalizedGreeting(String name) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.debug("Generating personalized greeting for {} at {}", name, timestamp);
        return String.format("Hello %s! Welcome to Nebula Auth Service. Current time: %s",
                           name, timestamp);
    }
}
```

**Repository 层（持久层）**:
- ✅ 数据访问（CRUD 操作）
- ✅ 继承 JpaRepository 获得基础方法
- ✅ 使用 Spring Data JPA 方法命名规则
- ✅ 复杂查询使用 @Query 注解
- ❌ 不包含业务逻辑
- ❌ 不直接被 Controller 调用

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA 自动实现查询（方法命名规则）
    Optional<User> findByUsername(String username);

    List<User> findByStatusAndIsDeleted(Integer status, Boolean isDeleted);

    // 使用 @Query 自定义查询
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    // 使用原生 SQL
    @Query(value = "SELECT * FROM user WHERE created_time > :date", nativeQuery = true)
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
}
```

**Entity 层（实体层）**:
- ✅ 使用 JPA 注解映射数据库表
- ✅ 定义表结构和字段关系
- ✅ 使用 Lombok 简化代码

```java
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
```

### 2.3 分层调用规则
```
Controller → Service → Repository
    ↓           ↓         ↓
  Result    Transaction  JPA
```

- **禁止跨层调用**: Controller 不能直接调用 Repository
- **单向依赖**: 上层依赖下层，下层不依赖上层
- **Repository 是接口**: 继承 JpaRepository，Spring 自动实现

### 2.4 Repository 命名规范

**接口命名**: `{Entity}Repository`
```java
✅ 正确:
public interface UserRepository extends JpaRepository<User, Long>
public interface UserRoleRepository extends JpaRepository<UserRole, Long>

❌ 错误:
public interface UserDao extends JpaRepository<User, Long>
public interface UserRepositoryDao extends JpaRepository<User, Long>
```

**方法命名规范**（Spring Data JPA 自动实现）:
```java
// 查询单个
Optional<User> findById(Long id);
Optional<User> findByUsername(String username);

// 查询列表
List<User> findAll();
List<User> findByStatus(Integer status);
List<User> findByStatusAndIsDeleted(Integer status, Boolean isDeleted);

// 统计
Long countByStatus(Integer status);
boolean existsByUsername(String username);

// 删除
void deleteById(Long id);
void deleteByUsername(String username);
```

**方法命名关键字**:
- `findBy`: 查询
- `countBy`: 统计
- `existsBy`: 判断存在
- `deleteBy`: 删除
- `And`, `Or`: 条件连接
- `Between`, `LessThan`, `GreaterThan`: 范围查询
- `Like`, `StartingWith`, `EndingWith`, `Containing`: 模糊查询
- `OrderBy`: 排序

### 2.5 Entity 实体规范

**类命名**: 与数据库表名对应，去掉下划线使用驼峰
```java
表名: user_order
类名: UserOrder
```

**必填字段**:
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "created_time", updatable = false)
private LocalDateTime createdTime;

@Column(name = "updated_time")
private LocalDateTime updatedTime;

@Column(name = "is_deleted")
private Boolean isDeleted = false;
```

**JPA 注解使用**:
- `@Entity`: 标记为实体类
- `@Table`: 指定表名
- `@Id`: 主键
- `@GeneratedValue`: 主键生成策略
- `@Column`: 字段映射（name 指定列名，nullable、unique 等约束）
- `@Transient`: 不映射到数据库
- `@PrePersist`: 保存前回调
- `@PreUpdate`: 更新前回调

---

## 3. API 设计规范

### 3.1 RESTful 设计原则
- 使用 HTTP 动词表达操作类型
- URL 表示资源，使用名词复数
- 使用 HTTP 状态码表示结果
- 无状态设计

### 3.2 URL 设计规范

```yaml
✅ 正确:
GET    /api/users          # 获取用户列表
GET    /api/users/{id}     # 获取指定用户
POST   /api/users          # 创建用户
PUT    /api/users/{id}     # 更新用户
DELETE /api/users/{id}     # 删除用户

❌ 错误:
GET    /api/getUsers       # URL 中包含动词
POST   /api/createUser     # URL 中包含动词
```

**资源嵌套**:
```yaml
GET    /api/users/{userId}/orders           # 获取用户的所有订单
POST   /api/users/{userId}/orders           # 为用户创建订单
```

**查询过滤**:
```yaml
GET    /api/users?page=1&size=10
GET    /api/users?status=active
```

### 3.3 响应格式规范

**统一响应结构**:
```json
{
  "code": 200,           // 响应码: 200成功, 其他失败
  "message": "Success",  // 响应消息
  "data": {},            // 响应数据
  "timestamp": "2026-01-03 14:30:00"  // 时间戳
}
```

**使用方式**:
```java
// 成功响应
Result.success(data);
Result.success(data, "Operation successful");

// 错误响应
Result.error("Internal server error");
Result.error(500, "Custom error message");

// 特定错误码
Result.validationError("Invalid input");    // 400
Result.notFound("User not found");          // 404
Result.unauthorized("Authentication required"); // 401
Result.forbidden("Access denied");          // 403
```

---

## 4. 日志规范

### 4.1 日志级别使用
- **ERROR**: 系统错误，需要立即处理
- **WARN**: 潜在问题，需要关注
- **INFO**: 重要业务流程、关键节点（Controller 层）
- **DEBUG**: 调试信息，用于问题排查（Service 层）

```java
// Controller 使用 INFO
@Slf4j
@RestController
public class HelloController {
    @GetMapping
    public Result<String> hello() {
        log.info("Hello endpoint called");  // 记录请求进入
    }
}

// Service 使用 DEBUG
@Slf4j
@Service
public class HelloService {
    public String getGreeting() {
        log.debug("Generating default greeting at {}", timestamp);  // 记录详细过程
    }
}
```

### 4.2 日志格式规范
- **占位符使用**: 使用 `{}` 而不是字符串拼接

```java
✅ 正确:
log.info("Processing request for user: {}", userId);
log.error("Failed to process", exception);

❌ 错误:
log.info("Processing request for user: " + userId);  (性能问题)
log.error("Failed: " + exception.getMessage());      (丢失堆栈)
```

### 4.3 日志记录场景
```java
// 1. 请求入口
log.info("GET /api/users from {}", request.getRemoteAddr());

// 2. 关键业务操作
log.info("User login successful: {}", username);

// 3. 异常情况
log.error("Database connection failed", exception);

// 4. 性能监控
log.info("Request completed in {} ms", duration);
```

---

## 5. 异常处理规范

### 5.1 异常分类
- **业务异常**: 可预期的业务错误（如用户不存在）
- **参数异常**: 请求参数验证失败
- **系统异常**: 不可预期的系统错误

### 5.2 异常处理原则
- **不要捕获所有异常**: 只捕获能处理的异常
- **异常转换**: 底层异常转换为业务异常
- **不要吞没异常**: 必须记录或向上抛出
- **提供有用信息**: 异常消息要包含上下文

### 5.3 统一异常处理（建议实现）
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.validationError(message);
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        log.warn("Business exception: {}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return Result.error("Internal server error");
    }
}
```

---

## 6. 依赖注入规范

### 6.1 强制使用构造器注入

```java
✅ 正确: 使用 Lombok @RequiredArgsConstructor
@Slf4j
@RestController
@RequiredArgsConstructor  // 自动生成构造器
public class HelloController {
    private final HelloService helloService;  // final 字段
}

❌ 错误: 字段注入
@Autowired
private HelloService helloService;

❌ 错误: Setter 注入
@Autowired
public void setHelloService(HelloService helloService) {
    this.helloService = helloService;
}
```

**为什么选择构造器注入**:
1. ✅ 保证依赖不可变（final）
2. ✅ 保证依赖不为空
3. ✅ 便于单元测试
4. ✅ Spring 官方推荐

---

## 7. 配置管理规范

### 7.1 配置文件组织
```
src/main/resources/
├── application.yml           # 通用配置
├── application-dev.yml       # 开发环境
└── application-prod.yml      # 生产环境
```

### 7.2 多环境配置
```yaml
# application.yml
spring:
  application:
    name: auth-service
  profiles:
    active: dev  # 激活的环境

# application-dev.yml
server:
  port: 8081

# application-prod.yml
server:
  port: 8080
```

### 7.3 敏感信息处理
- ❌ 不将敏感信息提交到代码仓库
- ✅ 使用环境变量或配置中心

```yaml
# 使用环境变量
spring:
  datasource:
    password: ${DB_PASSWORD:defaultPassword}
```

---

## 8. 数据库规范（JPA）

### 8.1 表命名规范
```sql
✅ 正确:
user           -- 用户表
user_order     -- 用户订单表
sys_dict       -- 系统字典表

❌ 错误:
User           (大写)
user-order     (使用连字符)
```

### 8.2 Entity 类命名规范
- 与数据库表名对应，去掉下划线使用驼峰
- 使用 JPA 注解映射表结构

```java
@Entity
@Table(name = "user_order")
public class UserOrder { }  // 表名 user_order → 类名 UserOrder
```

### 8.3 字段命名规范
```sql
✅ 正确（数据库字段）:
id             -- 主键
user_name      -- 用户名
created_time   -- 创建时间
is_deleted     -- 是否删除

❌ 错误:
userName       (驼峰)
```

**Java 字段映射**（使用 @Column 注解）:
```java
@Column(name = "user_name")
private String userName;  // 数据库 user_name → Java userName
```

**通用字段**:
```java
// 数据库字段
id              BIGINT        主键
created_time    DATETIME      创建时间
updated_time    DATETIME      更新时间
is_deleted      TINYINT       逻辑删除 0-未删除 1-已删除
created_by      VARCHAR(32)   创建人
updated_by      VARCHAR(32)   更新人

// Java 字段映射
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "created_time", updatable = false)
private LocalDateTime createdTime;

@Column(name = "updated_time")
private LocalDateTime updatedTime;

@Column(name = "is_deleted")
private Boolean isDeleted = false;
```

### 8.4 JPA 查询规范

**优先使用 Spring Data JPA 方法命名**:
```java
// ✅ 推荐：方法命名规则（自动实现）
Optional<User> findByUsername(String username);
List<User> findByStatusAndIsDeleted(Integer status, Boolean isDeleted);
```

**复杂查询使用 @Query**:
```java
// ✅ JPQL 查询
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findByEmail(@Param("email") String email);

// ✅ 原生 SQL（仅在必要时使用）
@Query(value = "SELECT * FROM user WHERE created_time > :date", nativeQuery = true)
List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
```

**避免 N+1 查询问题**:
```java
// ❌ 错误：会产生 N+1 查询
List<User> users = userRepository.findAll();
for (User user : users) {
    user.getOrders().size();  // 每次都查询一次订单表
}

// ✅ 正确：使用 @EntityGraph 或 JOIN FETCH
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();

// 或
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

### 8.5 事务管理规范

**Service 层方法添加事务注解**:
```java
@Service
public class UserService {

    // 查询方法使用只读事务
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 修改方法使用默认事务
    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    // 复杂业务逻辑使用事务
    @Transactional(rollbackFor = Exception.class)
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        // 业务逻辑...
    }
}
```

**事务注意事项**:
- 事务应该加在 Service 层，不要加在 Controller 或 Repository 层
- 查询方法使用 `@Transactional(readOnly = true)` 提升性能
- 涉及多个表修改的方法需要考虑事务传播行为
- 避免在事务中调用外部 API

---

## 9. 代码审查清单

提交代码前，请确认以下事项：

- [ ] **命名规范**: 包名全小写、类名大驼峰、方法名小驼峰
- [ ] **代码分层**: 分层清晰，职责单一
- [ ] **依赖注入**: 使用构造器注入，不使用 @Autowired 字段注入
- [ ] **Repository 规范**: 接口命名 `{Entity}Repository`，继承 JpaRepository
- [ ] **Entity 规范**: 包含通用字段（id、created_time、updated_time、is_deleted）
- [ ] **事务管理**: Service 层涉及数据修改的方法添加 @Transactional
- [ ] **查询优化**: 优先使用方法命名规则，避免 N+1 查询
- [ ] **注释规范**: 公共类和方法有 JavaDoc 注释
- [ ] **日志规范**: Controller 用 info，Service 用 debug，使用占位符 {}
- [ ] **异常处理**: 不吞没异常，异常信息包含上下文
- [ ] **参数验证**: 使用 @Valid 进行参数验证
- [ ] **API 响应**: 所有接口返回 Result<T> 类型
- [ ] **安全规范**: 敏感信息不硬编码，JPA 使用参数化查询

---

## 10. 参考标准

本规范参考以下业界标准制定：

- [阿里巴巴 Java 开发手册（泰山版）](https://github.com/alibaba/p3c)
- [Spring Boot 官方最佳实践](https://spring.io/guides/gs/spring-boot/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Clean Code (Robert C. Martin)](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Effective Java (Joshua Bloch)](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/)