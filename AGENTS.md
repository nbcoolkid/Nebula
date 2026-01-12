# Nebula 开发指南

本文档为 AI 编码代理提供 Nebula 项目的开发规范和命令参考。

## 项目结构
```
Nebula/
├── ui/          # Vue 3 + TypeScript 前端
├── server/      # Spring Boot 微服务后端
└── script/      # 构建和部署脚本
```

## 前端（ui/）命令
```bash
cd ui
pnpm install              # 安装依赖（Node >=20.19.0, pnpm >=8.8.0）
pnpm dev                  # 启动开发服务器
pnpm build                # 构建生产版本
pnpm lint                 # ESLint 检查
pnpm fix                  # ESLint 自动修复
pnpm lint:prettier        # Prettier 格式化
pnpm lint:stylelint       # Stylelint 检查并修复样式
```

## 后端（server/）命令
```bash
cd server
mvn clean install                # 编译打包所有模块
mvn clean install -DskipTests    # 跳过测试打包
mvn test                         # 运行所有测试
mvn test -Dtest=ClassName        # 运行单个测试类
mvn test -Dtest=ClassName#methodName  # 运行单个测试方法
mvn spring-boot:run              # 运行应用

# 启动/停止所有服务
cd script && sh start.sh && sh stop.sh

# Docker 部署
cd script/docker-compose && docker-compose up -d && docker-compose down
```

## 前端代码风格（Vue 3 + TypeScript）

### 导入与格式化
- 单引号，无分号，2 空格缩进，行宽 100 字符，无尾随逗号
- 使用 `@/` 别名引用 src 目录

```typescript
import { ref, computed } from 'vue'
import { useTableStore } from '@/store/modules/table'
```

### TypeScript 规范
- 严格模式 (`strict: true`)，接口命名 `Api.Auth.LoginParams`
- API 请求函数必须标注返回类型

```typescript
export function fetchLogin(params: Api.Auth.LoginParams) {
  return request.post<Api.Auth.LoginResponse>({ url: '/api/auth/login', params })
}
```

### Vue 组件规范
- 多词组件名（PascalCase），使用 `<script setup lang="ts">`
- Props 使用 `defineProps<Type>()`

```vue
<script setup lang="ts">
import { ref } from 'vue'
defineOptions({ name: 'ArtTable' })
interface Props { loading?: boolean }
const props = withDefaults(defineProps<Props>(), { loading: false })
</script>
```

### 样式规范
- 使用 SCSS，组件样式使用 `<style lang="scss" scoped>`

### Git 提交规范
遵循约定式提交：`feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`

## 后端代码风格（Spring Boot + JPA）

### 依赖注入
强制使用构造器注入（Lombok `@RequiredArgsConstructor`），禁止 `@Autowired`

```java
@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;
}
```

### 命名规范
- 包名：全小写，域名反写（`com.nebula.auth.controller`）
- 类名：大驼峰（`HelloController`），方法名：小驼峰动词开头（`getUserById`）
- 常量：全大写下划线（`DEFAULT_PAGE_SIZE`）

### 分层架构
```
Controller → Service → Repository
```

- **Controller**: 接收请求、参数验证、返回 `Result<T>`
- **Service**: 业务逻辑、事务控制
- **Repository**: 数据访问（继承 `JpaRepository`）
- **Entity**: 实体类，包含通用字段

```java
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
```

### 日志规范
- Controller 使用 `log.info()`，Service 使用 `log.debug()`
- 使用 `{}` 占位符

```java
log.info("Processing request for user: {}", userId);
log.error("Failed to process", exception);
```

### 响应格式
所有接口返回统一格式 `Result<T>`

```java
Result.success(data);
Result.success(data, "Operation successful");
Result.error("Internal server error");
Result.validationError("Invalid input");
Result.notFound("User not found");
```

### JPA 查询
优先使用方法命名规则

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByStatusAndIsDeleted(Integer status, Boolean isDeleted);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
```

### 事务管理
Service 层添加 `@Transactional`

```java
@Transactional(readOnly = true)
public User getUserById(Long id) { }

@Transactional
public void updateUser(User user) { }
```

### API 设计
RESTful 风格，URL 使用名词复数

```
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

## 技术栈
- 前端：Vue 3.5.21 + TypeScript 5.6.3 + Vite 7.1.5 + Element Plus 2.11.2 + Pinia 3.0.3
- 后端：Java 21 + Spring Boot 3.4.0 + Spring Cloud 2024.0.2 + Lombok + MapStruct

## 重要文件
- `/README.md` - 完整后端开发规范
- `/ui/package.json` - 前端依赖和脚本
- `/ui/eslint.config.mjs` - ESLint 配置
- `/server/pom.xml` - 后端 Maven 配置
