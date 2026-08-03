# ailearn-vue

AI Learn 学习平台的 SpringBoot + Vue 复刻版。

## 技术栈

- 后端：SpringBoot 3.3 + MyBatis-Plus + MySQL + JWT
- 前端：Vue 3 + Vite + Element Plus + Pinia + Vue Router

## 项目结构

```
ailearn-vue/
├── backend/      # SpringBoot 后端
├── frontend/     # Vue3 前端
├── sql/          # 数据库脚本
└── README.md
```

## 快速开始

### 后端

1. 创建 MySQL 数据库 `ailearn_vue`。
2. 执行 `sql/schema.sql` 初始化表结构。
3. 修改 `backend/src/main/resources/application.yml` 中的数据库配置。
4. 进入 `backend` 目录运行：`mvn spring-boot:run`

### 前端

1. 进入 `frontend` 目录运行：`npm install`
2. 运行：`npm run dev`

## 原项目

原项目为 Next.js + Prisma 版本： https://github.com/nzsybomm-create/ailearn.git
