# AI Learn (ailearn-vue)

基于 **Spring Boot 3 + Vue 3** 的 AI 学习平台，复刻自 Next.js + Prisma 原项目 `ailearn`。

## 技术栈

### 后端 (backend/)
- Spring Boot 3.3
- MyBatis-Plus 3.5（MySQL）
- Spring Security + JWT（鉴权）
- MySQL 8.0
- Java 17

### 前端 (frontend/)
- Vue 3 + Vite
- Element Plus（UI）
- Pinia（状态管理）
- Vue Router（路由）
- Axios（HTTP）

## 功能模块

| 模块 | 说明 |
| --- | --- |
| 认证 | 注册 / 登录 / 忘记密码 / 个人资料 |
| 课程中心 | 课程广场 / 详情 / 选课 / 我的课程 / 收藏 / 评价 |
| 课程内容 | 单元 / 课时 / 课件 / 学习进度 |
| 学习核心 | 题库 / 练习 / 测验 / 考试 / 作业（含自动判分与教师批改） |
| AI 助教 | 对话会话（Mock，预留真实 LLM 接入点） |
| 群聊 | 课程群聊 |
| 互动协作 | 讨论 / 评论 / 公告 / 笔记 |
| 社区激励 | 排行榜 / 徽章 / 学习感言 / 推荐 / 学习分析 / 错题本 |
| 计划提醒 | 学习计划 / 任务 / 私信 / 通知 |
| 教师端 | 仪表盘 / 课程管理 / 题库管理 / 作业批改 |

## 快速开始

### 1. 数据库
```bash
mysql -u root -p
source backend/src/main/resources/schema.sql
source backend/src/main/resources/data.sql
```
`data.sql` 含演示数据，可重复执行（使用 `INSERT IGNORE`）。
演示账号（密码均为 `123456`）：
- `student@ailearn.com`（学生）
- `teacher@ailearn.com`（教师）
- `admin@ailearn.com`（管理员）

修改 `backend/src/main/resources/application.yml` 中的数据库连接信息。

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
# 接口地址： http://localhost:8080/api
```

### 3. 启动前端
```bash
cd frontend
npm install
npm run dev
# 默认： http://localhost:5173
```

## 接口约定
- 统一前缀 `/api`
- 统一返回：`{ code, message, data }`
- 鉴权：`Authorization: Bearer <token>`
- 分页参数：`current`（页，从1开始）、`size`（每页条数）

## 项目结构
```
ailearn-vue/
├── backend/                 # Spring Boot 后端
│   └── src/main/java/com/ailearn/
│       ├── common/          # 统一响应/异常/分页
│       ├── config/          # MyBatis-Plus 分页/跨域
│       ├── security/        # JWT/鉴权/当前用户
│       ├── entity/          # 实体(33 张表映射)
│       ├── mapper/          # MyBatis-Plus Mapper
│       ├── service/         # 业务层
│       └── controller/      # 接口层
└── frontend/                # Vue 前端 (待实现)
```

## 与原项目差异
- 数据库由 Prisma/PostgreSQL 改为 MyBatis-Plus/MySQL
- AI 助教当前为 Mock 实现，预留 `AiTutorService.sendMessage` 接入真实 LLM
- 前端由 Ant Design + Next.js 改为 Element Plus + Vue 3
