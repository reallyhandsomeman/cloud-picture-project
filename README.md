# 📦 智能图像生成云平台

> 基于 **Spring Boot + Vue3** 的全栈云图库系统
> 支持：图片上传 / 审核机制 / 空间管理 / 以图搜图 / 多级缓存优化 / 多人协同编辑

---

## 🏗 项目架构

```text
Frontend   : Vue3 + Vite
Backend    : Spring Boot + MyBatis-Plus
Database   : MySQL 8.x
Cache      : Redis + Caffeine
Storage    : 腾讯云 COS
Auth       : Sa-Token
Docs       : Knife4j
```

---

## ⚙️ 环境准备

### 后端环境

* Java 8
* MySQL 8.x
* Redis（推荐 Docker 部署）
* Maven

### 前端环境

* Node.js 16+
* npm / pnpm

---

## 🚀 项目启动

### 1️⃣ 启动后端

```bash
mvn clean install
mvn spring-boot:run
```

接口文档地址：

```
http://localhost:8123/api/doc.html#/home
```

（已集成 Knife4j，可在线调试接口）

---

### 2️⃣ 启动前端

```bash
npm install
npm run dev
```

访问地址：

```
http://localhost:5173
```

---

# 🧩 核心功能模块

---

## 👤 用户模块

### 功能列表

* 用户注册
* 用户登录（自动脱敏）
* 用户登出
* 权限控制
* 管理员查询用户

### 权限设计

基于 **Spring AOP + 自定义注解** 实现统一权限拦截：

```java
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
```

* 支持角色校验
* 支持统一异常处理
* 支持扩展自定义权限逻辑

---

## 🖼 图片模块

### 1️⃣ 云存储接入

使用：腾讯云 COS

支持功能：

* 本地图片上传
* URL 上传
* 图片下载
* 管理员审核机制
* 用户可见性控制

⚠ 注意：

* 需要开启 COS 公有读权限
* 需要配置 CORS

---

### 2️⃣ 图片优化策略

#### ✅ WebP 压缩

* 自动压缩为 `.webp`
* 可选删除原图
* 显著降低存储成本

#### ✅ 文件类型自动识别

* 自动检测图片真实类型
* 修复 URL 上传无后缀问题
* 统一命名规则

---

### 3️⃣ 缓存优化设计

#### 一级缓存：Redis

* 热点数据缓存
* 首次访问 → DB
* 二次访问 → Redis
* 性能提升约 1 倍

#### 二级缓存：Caffeine

* 本地缓存
* 减少 Redis 访问压力
* 提升高频接口性能

---

### 4️⃣ 缩略图优化

* 存储原图 + 预览图
* 首页加载预览图
* 减少首屏加载时间

---

## 📁 空间模块

支持：

* 创建个人空间
* 私有空间
* 公共图库
* 管理员空间管理
* 空间使用统计分析

---

## 🔎 搜索功能

支持：

* 关键字搜索
* 主色调提取
* 以图搜图（需 acs-token）

⚠ 注意：

* 部分外链图片可能无法进行以图搜图
* 必须携带合法 token

---

## 📊 图库分析

* 公共图库统计
* 私人空间统计
* 空间存储占用分析

---

## 🔐 Sa-Token 鉴权

必须实现：

```java
getPermissionList()
```

否则鉴权会失败，导致空间数据无法访问。

---

## 👥 多人协同编辑

支持：

* 实时编辑
* 多人进入可见
* 编辑锁机制（只读控制）
* 操作实时同步

适用于团队空间协作场景。

---

# 🐳 Redis Docker 部署

```bash
docker pull redis
docker run -p 6379:6379 redis
```

---

# ⚠ 常见问题

---

## 1️⃣ 图片跨域问题（CORS）

报错示例：

```
No 'Access-Control-Allow-Origin' header
```

解决方案：

在腾讯云 COS 配置跨域规则：

```xml
<AllowedOrigin>http://localhost:5173</AllowedOrigin>
<AllowedMethod>GET</AllowedMethod>
<AllowedHeader>*</AllowedHeader>
```

---

## 2️⃣ 图片无法访问

原因：

COS 未开启 Public 读权限。

解决：

开启存储桶公有读。

---

## 3️⃣ 以图搜图失败

原因：

* 未携带 acs-token
* 外部图片源不支持

---

# 📈 性能优化总结

| 优化点  | 解决方案     |
| ---- | -------- |
| 高频查询 | Redis    |
| 本地热点 | Caffeine |
| 图片体积 | WebP 压缩  |
| 加载速度 | 缩略图      |
| 并发编辑 | 锁机制      |

---

# 🌟 技术亮点

* Spring AOP 权限切面
* 多级缓存架构
* WebP 自动压缩
* 图片真实类型识别
* 以图搜图能力
* 多人协同编辑
* Sa-Token 鉴权体系

---

# 🏁 推荐启动顺序

1. 启动 MySQL
2. 启动 Redis
3. 启动后端服务
4. 启动前端服务
5. 配置 COS 跨域规则

---
