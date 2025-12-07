# 丽水智慧旅游管理系统 - 接口示例与启动步骤

## 📋 项目概述

**项目名称**：lishui-smart-tourism-server（丽水智慧旅游管理系统后端）

**技术栈**：
- JDK 21
- Spring Boot 3.2.1
- MySQL 8.0
- Redis
- MyBatis-Plus 3.5.5
- LangChain4j 0.26.1（AI 功能）
- JWT 认证
- SpringDoc OpenAPI（接口文档）

---

## 🚀 快速启动步骤

### 1. 环境准备

#### 必需环境
- **JDK 21** - 安装并配置 JAVA_HOME
- **Maven 3.6+** - 用于构建项目
- **MySQL 8.0+** - 数据库服务
- **Redis** - 缓存服务

### 2. 数据库初始化

```bash
# 1. 登录 MySQL
mysql -u root -p

# 2. 执行建表脚本
source src/main/resources/sql/lishui_tourism.sql
```

或者直接导入：
```bash
mysql -u root -p < src/main/resources/sql/lishui_tourism.sql
```

### 3. 配置文件

编辑 `src/main/resources/application.yml`，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lishui_tourism?...
    username: root
    password: your_password  # 修改为你的 MySQL 密码
  
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果 Redis 有密码，填写这里
```

### 4. 启动项目

```bash
# 进入项目根目录
cd h:\JavaProject\lishui-tourism

# 使用 Maven 编译并启动
mvn spring-boot:run

# 或先打包再运行
mvn clean package
java -jar target/lishui-smart-tourism-server-1.0.0.jar
```

### 5. 验证启动

启动成功后，访问：
- **Swagger 文档**：http://localhost:8080/api/swagger-ui.html
- **API 文档 JSON**：http://localhost:8080/api/v3/api-docs

---

## 📖 核心接口示例

### 1️⃣ 用户认证模块

#### 1.1 用户注册

**接口**：`POST /api/user/register`

**请求示例**：
```json
{
  "username": "tourist01",
  "password": "123456",
  "nickname": "张三",
  "email": "tourist@example.com",
  "phone": "13800138000"
}
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

---

#### 1.2 用户登录

**接口**：`POST /api/user/login`

**请求示例**：
```json
{
  "username": "tourist01",
  "password": "123456"
}
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidG91cmlzdDAxIiwicm9sZSI6IlRPVVJJU1QiLCJpYXQiOjE3MDEwMDAwMDAsImV4cCI6MTcwMTYwNDgwMH0.xxx",
    "user": {
      "id": 1,
      "username": "tourist01",
      "nickname": "张三",
      "role": "TOURIST",
      "status": 1,
      "createdAt": "2025-12-07 10:00:00"
    }
  }
}
```

**重要**：登录后获取的 `token`，后续请求需要在 Header 中携带：
```
Authorization: Bearer <your_token>
```

---

#### 1.3 获取当前用户信息

**接口**：`GET /api/user/me`

**Header**：
```
Authorization: Bearer <your_token>
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "tourist01",
    "nickname": "张三",
    "role": "TOURIST",
    "status": 1
  }
}
```

---

### 2️⃣ 景点管理模块

#### 2.1 获取景点列表

**接口**：`GET /api/scenic-spot/list`

**参数**：
- `page`: 页码（默认 1）
- `size`: 每页数量（默认 10）
- `keyword`: 搜索关键词（可选）
- `destinationId`: 目的地 ID（可选）

**请求示例**：
```
GET /api/scenic-spot/list?page=1&size=10&keyword=仙都
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "缙云仙都景区",
        "description": "仙都景区是国家级风景名胜区...",
        "cover": "https://example.com/image.jpg",
        "ticketPrice": 60.00,
        "openingHours": "08:00-17:00",
        "level": "5A",
        "hotScore": 95,
        "rating": 4.80,
        "status": 1
      }
    ],
    "total": 1,
    "current": 1,
    "size": 10
  }
}
```

---

#### 2.2 获取景点详情

**接口**：`GET /api/scenic-spot/{id}`

**请求示例**：
```
GET /api/scenic-spot/1
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "缙云仙都景区",
    "description": "仙都景区是国家级风景名胜区...",
    "address": "浙江省丽水市缙云县仙都街道",
    "ticketPrice": 60.00,
    "openingHours": "08:00-17:00",
    "trafficGuide": "自驾或乘公交前往...",
    "level": "5A",
    "hotScore": 95,
    "rating": 4.80
  }
}
```

---

#### 2.3 获取热门景点

**接口**：`GET /api/scenic-spot/hot`

**参数**：
- `limit`: 数量限制（默认 10）

**请求示例**：
```
GET /api/scenic-spot/hot?limit=5
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "缙云仙都景区",
      "cover": "https://example.com/image.jpg",
      "hotScore": 95
    },
    {
      "id": 2,
      "name": "古堰画乡",
      "cover": "https://example.com/image2.jpg",
      "hotScore": 88
    }
  ]
}
```

---

### 3️⃣ AI 智能服务模块 ⭐ 核心功能

#### 3.1 景点智能问答

**接口**：`POST /api/ai/spot-chat`

**Header**：
```
Authorization: Bearer <your_token>
```

**请求示例**：
```json
{
  "spotId": 1,
  "question": "这个景点的门票多少钱？有什么优惠吗？"
}
```

**响应示例**（Mock 模式）：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": "根据景点信息，该景点门票价格适中，性价比较高。建议您提前在网上预订可享受优惠。"
}
```

**其他问题示例**：
```json
// 问开放时间
{
  "spotId": 1,
  "question": "景区什么时候开放？"
}

// 问交通
{
  "spotId": 1,
  "question": "怎么去这个景点？"
}

// 问推荐
{
  "spotId": 1,
  "question": "这个景点有什么特色？值得去吗？"
}
```

---

#### 3.2 AI 行程生成

**接口**：`POST /api/ai/itinerary`

**Header**：
```
Authorization: Bearer <your_token>
```

**请求示例**：
```json
{
  "days": 3,
  "budget": 1500,
  "preference": "自然风光+人文历史",
  "season": "春季"
}
```

**响应示例**（Mock 模式返回结构化 JSON）：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "title": "丽水精品3日游",
    "days": 3,
    "totalBudget": 1500,
    "itinerary": [
      {
        "day": 1,
        "title": "第一天：古堰画乡文化之旅",
        "spots": [
          {
            "name": "古堰画乡",
            "arriveTime": "09:00",
            "duration": 180,
            "description": "游览古堰画乡，感受江南水乡的宁静与艺术氛围",
            "cost": 0
          },
          {
            "name": "丽水市区",
            "arriveTime": "14:00",
            "duration": 120,
            "description": "市区休闲购物，品尝当地美食",
            "cost": 150
          }
        ],
        "accommodation": "丽水市区酒店",
        "accommodationCost": 200,
        "dailyBudget": 350
      },
      {
        "day": 2,
        "title": "第二天：缙云仙都仙境探秘",
        "spots": [
          {
            "name": "缙云仙都景区",
            "arriveTime": "08:30",
            "duration": 300,
            "description": "游览5A级景区仙都，欣赏奇峰异石、山水画卷",
            "cost": 60
          }
        ],
        "accommodation": "缙云县城酒店",
        "accommodationCost": 180,
        "dailyBudget": 440
      },
      {
        "day": 3,
        "title": "第三天：返程与购物",
        "spots": [
          {
            "name": "特产购物",
            "arriveTime": "10:00",
            "duration": 120,
            "description": "购买丽水特产：香菇、木耳、蜂蜜等",
            "cost": 200
          }
        ],
        "accommodation": null,
        "accommodationCost": 0,
        "dailyBudget": 200
      }
    ],
    "tips": [
      "建议穿着舒适的运动鞋，方便徒步游览",
      "随身携带防晒用品和雨具",
      "提前预订景区门票可享优惠",
      "尝试当地特色美食：缙云烧饼、丽水土鸡煲"
    ]
  }
}
```

---

#### 3.3 获取聊天历史

**接口**：`GET /api/ai/chat-history`

**Header**：
```
Authorization: Bearer <your_token>
```

**参数**：
- `spotId`: 景点 ID（可选，不传则返回所有）
- `limit`: 数量限制（默认 10）

**请求示例**：
```
GET /api/ai/chat-history?spotId=1&limit=10
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "spotId": 1,
      "question": "这个景点的门票多少钱？",
      "answer": "根据景点信息，该景点门票价格适中...",
      "createdAt": "2025-12-07 10:30:00"
    }
  ]
}
```

---

#### 3.4 获取我的行程列表

**接口**：`GET /api/ai/my-itineraries`

**Header**：
```
Authorization: Bearer <your_token>
```

**响应示例**：
```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "title": "丽水精品3日游",
      "days": 3,
      "budget": 1500,
      "preference": "自然风光+人文历史",
      "season": "春季",
      "itineraryData": "{...}",
      "createdAt": "2025-12-07 10:00:00"
    }
  ]
}
```

---

### 4️⃣ 管理员功能示例

#### 4.1 用户列表（需要管理员权限）

**接口**：`GET /api/user/list`

**Header**：
```
Authorization: Bearer <admin_token>
```

**参数**：
- `page`: 页码
- `size`: 每页数量
- `keyword`: 搜索关键词（可选）
- `role`: 角色筛选（可选：TOURIST/GUIDE/ADMIN）

**请求示例**：
```
GET /api/user/list?page=1&size=10&role=TOURIST
```

---

#### 4.2 启用/禁用用户

**接口**：`PUT /api/user/{userId}/status`

**Header**：
```
Authorization: Bearer <admin_token>
```

**参数**：
- `status`: 状态（0-禁用，1-启用）

**请求示例**：
```
PUT /api/user/1/status?status=0
```

---

## 🔑 角色权限说明

### 角色类型
1. **TOURIST**（游客）- 默认注册角色
   - 查看景点信息
   - 使用 AI 问答和行程生成
   - 发表评论、收藏

2. **GUIDE**（导游）
   - 游客所有权限
   - 管理导游线路
   - 创建线路和关联景点

3. **ADMIN**（管理员）
   - 所有权限
   - 用户管理（启用/禁用/分配角色）
   - 景点管理（CRUD）
   - 内容管理

### 测试账号

数据库初始化后会自动创建以下测试账号：

| 用户名    | 密码       | 角色    | 说明     |
|----------|-----------|---------|---------|
| admin    | admin123  | ADMIN   | 管理员   |
| guide01  | guide123  | GUIDE   | 导游     |
| tourist01| tourist123| TOURIST | 游客     |

---

## ⚙️ AI 模式配置

### Mock 模式（默认）

**优点**：无需配置 API Key，开箱即用  
**适用**：本地开发、测试、演示

配置文件：
```yaml
langchain4j:
  mock-mode: true  # 启用 Mock 模式
```

### 真实 AI 模式

如需使用真实的 OpenAI API：

```yaml
langchain4j:
  mock-mode: false  # 关闭 Mock 模式
  openai:
    api-key: sk-your-openai-api-key  # 你的 OpenAI API Key
    model-name: gpt-3.5-turbo
    base-url: https://api.openai.com/v1
```

或通过环境变量：
```bash
export OPENAI_API_KEY=sk-your-openai-api-key
```

---

## 📊 项目结构

```
lishui-smart-tourism-server/
├── src/main/java/com/lishui/tourism/
│   ├── common/                    # 通用模块
│   │   ├── constant/             # 常量定义
│   │   ├── context/              # 用户上下文
│   │   ├── exception/            # 异常处理
│   │   └── result/               # 统一返回
│   ├── config/                   # 配置类
│   │   ├── security/             # 安全配置（JWT）
│   │   ├── LangChain4jConfig     # AI 配置
│   │   ├── RedisConfig           # Redis 配置
│   │   └── WebMvcConfig          # MVC 配置
│   ├── controller/               # 控制器
│   │   ├── UserController        # 用户控制器
│   │   ├── ScenicSpotController  # 景点控制器
│   │   └── AiController          # AI 控制器
│   ├── dto/                      # 数据传输对象
│   ├── entity/                   # 实体类
│   ├── mapper/                   # MyBatis Mapper
│   ├── service/                  # 业务服务
│   │   ├── ai/                   # AI 服务
│   │   │   └── MockChatModel     # Mock AI 实现
│   │   ├── UserService           # 用户服务
│   │   ├── ScenicSpotService     # 景点服务
│   │   ├── AiService             # AI 服务
│   │   └── RedisService          # Redis 服务
│   ├── vo/                       # 视图对象
│   └── LishuiTourismApplication  # 启动类
├── src/main/resources/
│   ├── application.yml           # 配置文件
│   └── sql/
│       └── lishui_tourism.sql    # 数据库脚本
└── pom.xml                       # Maven 配置
```

---

## 🧪 测试流程

### 1. 用户注册和登录
```bash
# 1. 注册用户
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test01","password":"123456","nickname":"测试用户"}'

# 2. 登录获取 Token
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test01","password":"123456"}'
```

### 2. 查看景点
```bash
# 获取景点列表
curl http://localhost:8080/api/scenic-spot/list

# 获取热门景点
curl http://localhost:8080/api/scenic-spot/hot?limit=5

# 获取景点详情
curl http://localhost:8080/api/scenic-spot/1
```

### 3. 使用 AI 功能
```bash
# 景点问答（需要登录）
curl -X POST http://localhost:8080/api/ai/spot-chat \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{"spotId":1,"question":"这个景点门票多少钱？"}'

# 生成行程
curl -X POST http://localhost:8080/api/ai/itinerary \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{"days":3,"budget":1500,"preference":"自然风光"}'
```

---

## ❗ 常见问题

### 1. 启动失败：端口占用
**问题**：`Port 8080 was already in use`  
**解决**：修改 `application.yml` 中的 `server.port` 为其他端口

### 2. 数据库连接失败
**问题**：`Could not create connection to database`  
**检查**：
- MySQL 是否启动
- 用户名密码是否正确
- 数据库 `lishui_tourism` 是否创建

### 3. Redis 连接失败
**问题**：`Unable to connect to Redis`  
**检查**：
- Redis 是否启动
- 端口是否正确（默认 6379）

### 4. Token 无效
**问题**：返回 `Token 无效`  
**解决**：
- 检查 Token 是否过期（默认 7 天）
- 检查 Header 格式：`Authorization: Bearer <token>`
- 重新登录获取新 Token

---

## 📝 总结

本项目是一个 **完整可运行** 的 Spring Boot 3.x + JDK 21 智慧旅游后端系统，核心特性：

✅ **JWT 认证** - 完整的用户注册、登录、权限控制  
✅ **MyBatis-Plus** - 优雅的 ORM 操作  
✅ **Redis 缓存** - 热门数据缓存、Token 黑名单  
✅ **AI 智能服务** - 景点问答 + 行程生成（支持 Mock 降级）  
✅ **Swagger 文档** - 自动生成接口文档  
✅ **统一异常处理** - 全局异常拦截  
✅ **参数校验** - 请求参数自动校验  

**无需 OpenAI API Key 即可完整运行和测试所有功能！**

---

## 📧 技术支持

如遇问题，请检查：
1. 日志输出（控制台）
2. Swagger 文档：http://localhost:8080/api/swagger-ui.html
3. 数据库数据是否正确初始化

**祝您使用愉快！** 🎉
