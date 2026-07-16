# 工业运维决策智能体 (Industrial AI Platform)

> 基于 **LangChain4j + RAG + Spring Boot** 构建的工业设备智能运维平台
> 面向工业设备运维场景，提供智能问答、故障诊断、运维报告自动生成与知识库管理能力

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)
![LangChain4j](https://img.shields.io/badge/LangChain4j-1.1.0-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-8.2-green)
![Vue](https://img.shields.io/badge/Vue-3.5-42b883)

---

## 📖 项目简介

传统工业运维依赖人工经验，效率低、标准化程度不足。本项目构建了一个基于大语言模型的**工业运维决策智能体**，将 LLM 能力工程化落地为四大产品功能：

| 功能 | 说明 |
|------|------|
| 💬 **智能问答** | 多轮对话记忆 + RAG 检索增强，基于知识库回答工业运维问题 |
| 🔧 **故障诊断** | 融合知识库、历史故障记录、历史运维报告的多维信息智能诊断 |
| 📊 **报告生成** | 基于设备运行日志自动生成标准化运维分析报告 |
| 📚 **知识管理** | 工业文档（PDF/Word/TXT）上传、解析、向量化与检索 |

## 🏗 系统架构

```
┌─────────────────────────────────────────────────┐
│   前端展示层  Vue 3 + Element Plus + Axios        │
├─────────────────────────────────────────────────┤
│   API 接口层  4 个 REST Controller               │
│   (Chat / Fault / Document / Report)            │
├───────────────────────┬─────────────────────────┤
│  业务服务层 (5 Service) │  AI 能力层 (LangChain4j) │
│  对话·诊断·报告·文档·RAG │  通义千问·嵌入模型·RAG检索  │
├───────────────────────┴─────────────────────────┤
│   数据存储层  MongoDB (4 集合)                    │
│              + InMemoryEmbeddingStore (向量存储) │
└─────────────────────────────────────────────────┘
```

**后端分层：** Controller → Service → DAO（Spring Data MongoDB），四大实体：`ChatMessage`、`FaultRecord`、`IndustrialDocument`、`OperateReport`

## 🔍 RAG 知识库设计

```
文档上传 → Apache Tika 解析 → Recursive 分块 → 向量嵌入 → 向量存储 → 相似度检索 → 注入 Prompt → LLM 回答
```

核心设计要点：

- **分块策略**：Recursive 分块，500 token/块，50 token 重叠 —— 避免语义截断
- **检索质量**：余弦相似度阈值 ≥ 0.7 + Top3 返回 —— 平衡相关性与信息量
- **嵌入模型**：通义 `text-embedding-v4`
- **动态更新**：文档上传/删除后自动触发知识库重建（`@PostConstruct` 启动自动构建）
- **抑制幻觉**：检索结果 + 预设 Prompt 模板注入，限定大模型回答边界

## 🧠 智能对话：多轮记忆机制

- 自动拼接**最近 20 条历史对话**作为上下文，实现跨轮次记忆
- 支持普通对话与 RAG 检索增强两种模式
- 开启联网搜索（`enableSearch: true`）补充实时信息
- 对话历史持久化至 MongoDB，支持按 `sessionId` 回溯

## 🔧 故障诊断：多维信息融合

```
故障现象输入 → RAG 检索知识库 → 查询历史故障记录 → 查询历史运维报告 → 融合构建 Prompt → LLM 综合诊断 → 结果持久化
```

Prompt 工程实践：角色限定（资深工业故障诊断专家）+ 上下文注入 + 约束规则（防幻觉）+ 格式规范

## 📡 API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/chat` | 智能对话（多轮记忆） |
| POST | `/api/ragChat` | RAG 检索增强问答 |
| GET | `/api/history/{sessionId}` | 查询会话历史 |
| POST | `/api/diagnose` | 提交故障诊断 |
| GET | `/api/faults/{deviceId}` | 查询设备故障记录 |
| POST | `/api/document` | 上传工业文档 |
| GET | `/api/document` | 获取文档列表 |
| GET | `/api/device/{type}` | 按设备类型筛选文档 |
| GET | `/api/id/{id}` | 按 ID 查看文档 |
| DELETE | `/api/id/{id}` | 删除文档 |
| POST | `/api/generateReport` | 生成运维报告 |
| GET | `/api/reports/{deviceId}` | 查询设备报告 |

## 🛠 技术栈

| 类别 | 技术 | 选型理由 |
|------|------|----------|
| 后端框架 | Spring Boot 4.1.0 | 企业级 Java 框架，自动配置 + 起步依赖 |
| AI 框架 | LangChain4j 1.1.0 | Java 生态的 LangChain 实现，RAG/文档解析/嵌入一站式 |
| 大模型 | 通义千问 qwen3.7-max | 中文理解强，DashScope API 稳定，支持联网搜索 |
| 嵌入模型 | text-embedding-v4 | 与千问同生态，RAG 向量化 |
| 数据库 | MongoDB 8.2 | 文档型 NoSQL，适合聊天记录/报告等非结构化数据 |
| 文档解析 | Apache Tika | 支持 PDF/Word/TXT 多格式，与 LangChain4j 无缝集成 |
| 前端 | Vue 3 + Element Plus + Vite | 组合式 API + 企业级组件库 |

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.9+
- MongoDB 8.x（本地默认端口 27017）
- 阿里云 [DashScope API Key](https://dashscope.aliyun.com/)（通义千问）

### 启动后端

```bash
# 1. 克隆仓库
git clone https://github.com/Hef4664/industrial-ai-platform.git
cd industrial-ai-platform

# 2. 配置 API Key（不要把 Key 写进代码库！）
#    Windows (PowerShell)
$env:DASHSCOPE_API_KEY="你的APIKey"
#    Linux / macOS
export DASHSCOPE_API_KEY="你的APIKey"

# 3. 启动 MongoDB（默认 mongodb://localhost:27017/industrial_ai_db）

# 4. 启动应用
./mvnw spring-boot:run
```

后端启动于 `http://localhost:8080`。

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

## 📁 项目结构

```
industrial-ai-platform/
├── src/main/java/com/shengda/industrialaiplatform/
│   ├── configuration/       # Spring 配置（CORS 等）
│   ├── controller/          # 4 个 REST Controller
│   ├── service/             # 5 个业务 Service 接口
│   │   └── impl/            # 实现（含对话 V1/V2 迭代版本）
│   ├── dao/                 # Spring Data MongoDB DAO
│   └── entity/              # 4 个 MongoDB 实体
├── src/main/resources/
│   └── application.yml      # 配置文件（API Key 从环境变量读取）
├── frontend/                # Vue 3 前端
│   ├── src/
│   │   ├── App.vue          # 主页面（4 大功能标签页）
│   │   └── main.js          # 入口 + Element Plus 注册
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
└── pom.xml
```

## 🎯 迭代记录

- **V1 基础对话**：直接调用大模型，无记忆
- **V2 智能升级**：多轮记忆（20 条历史）+ RAG 检索 + 联网搜索 + 消息持久化

## 📌 后续规划

- [ ] 向量存储迁移至 Milvus，支持大规模知识库
- [ ] 引入 Function Calling / MCP，扩展 Agent 工具调用能力
- [ ] 构建评估测试集，量化检索命中率与诊断准确率
- [ ] 多智能体协作架构探索

## 👤 作者

**付宏恩** — 数据科学与大数据技术专业
📧 3284312038@qq.com · 🐙 [github.com/Hef4664](https://github.com/Hef4664)

## 📄 License

本项目仅供学习交流使用。
