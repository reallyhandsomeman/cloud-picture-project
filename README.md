# 📦 智能图像生成云平台

基于 **Spring Boot + MySQL + Redis + COS + Vue3** 的全栈云图库系统，支持用户上传、图片审核、空间管理、以图搜图、缓存优化、多人协同编辑等功能。

---

## 一、项目架构

```
前端：Vue3 + Vite
后端：Spring Boot + MyBatis-Plus
数据库：MySQL 8.x
缓存：Redis + Caffeine
对象存储：腾讯云 COS
鉴权：Sa-Token
```

---

## 二、环境准备

### 1️⃣ 后端环境

* Java 8
* MySQL 8.x
* Redis（推荐 Docker）
* Maven

### 2️⃣ 前端环境

* Node.js 16+
* npm / pnpm

---

## 三、项目初始化

### 后端启动

```bash
mvn clean install
mvn spring-boot:run
```

接口文档：

```
http://localhost:8123/api/doc.html#/home
```

（已集成 Knife4j）

---

### 前端启动

```bash
npm install
npm run dev
```

访问：

```
http://localhost:5173
```

---

## 四、核心功能模块

---

# 👤 用户模块

### 功能

* 用户注册
* 用户登录（脱敏处理）
* 用户登出
* 权限控制
* 管理员查询用户

### 权限设计

基于 **Spring AOP + 自定义注解** 实现统一权限拦截。

```java
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
```

通过环绕通知进行权限校验。

---

# 🖼 图片模块

## 1️⃣ 云存储接入

使用：

* 腾讯云 COS

功能：

* 图片上传
* URL 上传
* 审核机制
* 管理员审批
* 图片下载

> 注意：COS 需要开启 CORS 和 public 读权限

---

## 2️⃣ 图片优化

### WebP压缩

* 自动压缩为 webp
* 可选择删除原图
* 节约存储空间

### 文件类型识别

服务器自动识别图片后缀，避免 URL 上传无扩展名问题。

---

## 3️⃣ 缓存优化

### Redis 缓存

* 热门数据缓存
* 首次读 DB
* 二次读 Redis（速度提升约一倍）

### 本地缓存 Caffeine

* 二级缓存
* 减少 Redis 压力

---

## 4️⃣ 缩略图优化

* 存储原图 + 预览图
* 首页加载预览图
* 提升加载速度

---

# 空间模块

支持：

* 创建空间
* 管理员管理空间
* 私有空间
* 公共图库

支持空间使用统计分析。

---

# 搜索功能

### 图片搜索

* 关键字搜索
* 主色调提取
* 以图搜图（需 acs-token）

> 以图搜图对图片 URL 有要求，部分外链可能失败

---

# 图库分析

* 公共图库统计
* 私人空间分析
* 空间占用分析

---

# Sa-Token 鉴权

需要实现：

```java
getPermissionList()
```

否则鉴权永远失败。

---

# 多人协同编辑

支持：

* 实时编辑
* 多人进入同步
* 只读锁定
* 实时状态同步

---

# 🚀 Redis 安装（Docker）

```bash
docker pull redis
docker run -p 6379:6379 redis
```

---

# ⚠ 常见问题

## 1️⃣ 图片跨域问题

报错：

```
No 'Access-Control-Allow-Origin' header
```

解决：

腾讯云 COS → 跨域设置：

```xml
<AllowedOrigin>http://localhost:5173</AllowedOrigin>
<AllowedMethod>GET</AllowedMethod>
<AllowedHeader>*</AllowedHeader>
```

---

## 2️⃣ 图片不可见

原因：

COS 未开启 public 访问。

解决：

开启公有读权限。

---

## 3️⃣ 以图搜图不稳定

原因：

* 必须携带 acs-token
* 部分 URL 无法被识别

---

# 性能优化总结

| 优化点  | 方案       |
| ---- | -------- |
| 高频查询 | Redis    |
| 本地热点 | Caffeine |
| 图片体积 | WebP     |
| 加载速度 | 缩略图      |
| 并发编辑 | 锁机制      |

---

# 技术亮点

* Spring AOP 权限切面
* 多级缓存架构
* 图片自动类型识别
* WebP 压缩优化
* 以图搜图
* 实时协同编辑
* Sa-Token 权限控制

---

# 启动顺序建议

1. 启动 MySQL
2. 启动 Redis
3. 启动后端
4. 启动前端
5. 配置 COS 跨域

---
