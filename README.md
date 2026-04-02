# 护士排班系统 - 后端API

## 项目概述

基于Spring Boot的护士排班系统后端接口，为微信小程序提供RESTful API服务。

## 技术栈

- **Java 11**
- **Spring Boot 2.7.18**
- **MySQL 8.0**
- **Redis**
- **RabbitMQ**
- **MyBatis-Plus 3.5.3.1**
- **JWT (io.jsonwebtoken 0.11.5)**
- **Lombok**
- **Fastjson 2.0.25**
- **Hutool 5.8.16**

## 项目结构

```
backend/
├── source/                          # MyBatis Mapper XML文件（手写SQL）
│   ├── UserMapper.xml
│   ├── DepartmentMapper.xml
│   ├── DepartmentMemberMapper.xml
│   ├── ShiftMapper.xml
│   ├── ScheduleMapper.xml
│   ├── LeaveApplyMapper.xml
│   └── ExpectScheduleMapper.xml
├── src/main/
│   ├── java/com/nurse/scheduling/
│   │   ├── common/                  # 通用模块
│   │   │   ├── Result.java         # 统一响应结果
│   │   │   ├── ResultCode.java     # 响应状态码枚举
│   │   │   ├── BusinessException.java  # 业务异常
│   │   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   │   ├── config/                  # 配置类
│   │   │   ├── MyBatisPlusConfig.java  # MyBatis-Plus配置
│   │   │   ├── RedisConfig.java    # Redis配置
│   │   │   ├── JwtInterceptor.java # JWT拦截器
│   │   │   ├── WebConfig.java      # Web配置（跨域、拦截器）
│   │   │   └── RabbitMqConfig.java # RabbitMQ配置
│   │   ├── controller/              # 控制器层
│   │   │   ├── UserController.java
│   │   │   ├── DepartmentController.java
│   │   │   ├── ShiftController.java
│   │   │   └── ScheduleController.java
│   │   ├── dto/                     # DTO数据传输对象
│   │   │   ├── user/               # 用户相关DTO
│   │   │   ├── department/         # 科室相关DTO
│   │   │   ├── shift/              # 班种相关DTO
│   │   │   ├── schedule/           # 排班相关DTO
│   │   │   ├── leave/              # 假勤申请DTO
│   │   │   ├── expect/             # 期望排班DTO
│   │   │   └── statistics/         # 统计相关DTO
│   │   ├── entity/                  # 实体类
│   │   │   ├── BaseEntity.java     # 基础实体
│   │   │   ├── User.java
│   │   │   ├── Department.java
│   │   │   ├── DepartmentMember.java
│   │   │   ├── Shift.java
│   │   │   ├── Schedule.java
│   │   │   ├── LeaveApply.java
│   │   │   └── ExpectSchedule.java
│   │   ├── mapper/                  # Mapper接口
│   │   │   ├── UserMapper.java
│   │   │   ├── DepartmentMapper.java
│   │   │   ├── DepartmentMemberMapper.java
│   │   │   ├── ShiftMapper.java
│   │   │   ├── ScheduleMapper.java
│   │   │   ├── LeaveApplyMapper.java
│   │   │   └── ExpectScheduleMapper.java
│   │   ├── service/                 # 服务接口
│   │   │   ├── UserService.java
│   │   │   ├── DepartmentService.java
│   │   │   ├── ShiftService.java
│   │   │   └── ScheduleService.java
│   │   ├── service/impl/            # 服务实现
│   │   │   ├── UserServiceImpl.java
│   │   │   ├── DepartmentServiceImpl.java
│   │   │   ├── ShiftServiceImpl.java
│   │   │   └── ScheduleServiceImpl.java
│   │   ├── util/                    # 工具类
│   │   │   ├── JwtUtil.java         # JWT工具类
│   │   │   └── RedisUtil.java       # Redis工具类
│   │   └── NurseSchedulingApplication.java  # 启动类
│   └── resources/
│       ├── application.yml          # 应用配置
│       └── db_init.sql              # 数据库初始化脚本
├── pom.xml                          # Maven配置
└── README.md                        # 项目说明
```

## 核心功能模块

### 1. 用户模块
- 微信登录
- 获取用户信息
- 更新用户信息

### 2. 科室模块
- 创建科室
- 加入科室
- 获取科室信息
- 解散科室
- 转让科室
- 成员管理（列表、踢出、详情）

### 3. 班种模块
- 班种列表查询
- 创建班种
- 更新班种
- 删除班种

### 4. 排班模块
- 月排班查询
- 周排班查询
- 添加排班
- 更新排班
- 删除排班
- 我的排班查询

## 数据库设计

### 数据表
1. **sys_user** - 用户表
2. **sys_department** - 科室表
3. **sys_department_member** - 科室成员关联表
4. **biz_shift** - 班种表
5. **biz_schedule** - 排班表
6. **biz_leave_apply** - 假勤申请表
7. **biz_expect_schedule** - 期望排班表

## 快速开始

### 1. 环境准备

确保以下环境已安装：
- JDK 11
- Maven 3.6+
- MySQL 8.0
- Redis
- RabbitMQ（可选）

### 2. 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -p < src/main/resources/db_init.sql

# 或手动执行SQL脚本
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nurse_scheduling
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

### 4. 启动项目

```bash
# 编译打包
mvn clean package

# 运行
java -jar target/scheduling-system-1.0.0.jar

# 或使用Maven运行
mvn spring-boot:run
```

### 5. 访问接口

项目启动后访问：`http://localhost:8080/api`

## API接口文档

### 统一响应格式

```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "data": {}
}
```

### 主要接口

#### 用户接口
- `POST /api/user/wxLogin` - 微信登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息

#### 科室接口
- `POST /api/department/create` - 创建科室
- `POST /api/department/join` - 加入科室
- `GET /api/department/info` - 获取科室信息
- `DELETE /api/department/dismiss` - 解散科室
- `POST /api/department/transfer` - 转让科室
- `GET /api/department/members` - 获取成员列表
- `POST /api/department/kick` - 踢出成员
- `GET /api/department/invite` - 获取邀请链接
- `GET /api/department/member/{memberId}` - 获取成员详情
- `PUT /api/department/member/{memberId}` - 更新成员信息

#### 班种接口
- `GET /api/shift/list` - 获取班种列表
- `POST /api/shift/create` - 创建班种
- `PUT /api/shift/update` - 更新班种
- `DELETE /api/shift/delete/{id}` - 删除班种

#### 排班接口
- `GET /api/schedule/monthly` - 获取月排班
- `GET /api/schedule/weekly` - 获取周排班
- `POST /api/schedule/add` - 添加排班
- `PUT /api/schedule/update` - 更新排班
- `DELETE /api/schedule/delete/{id}` - 删除排班
- `GET /api/schedule/my` - 获取我的排班

## 技术亮点

### 1. 统一响应处理
- 使用 `Result<T>` 统一封装响应数据
- 全局异常处理，统一错误返回格式

### 2. DTO分层设计
- Controller层严格使用DTO作为入参和响应
- 避免直接暴露实体类，提高安全性

### 3. JWT认证
- 基于JWT的用户认证
- 拦截器自动验证token，获取用户信息

### 4. MyBatis-Plus增强
- 自动填充创建时间和更新时间
- 逻辑删除支持
- 分页插件

### 5. 手写SQL
- 复杂查询使用XML文件编写SQL
- 便于SQL优化和维护

### 6. 日志记录
- 使用 `@Slf4j` 注解
- 关键操作打印日志，便于问题排查

## 注意事项

1. **日志文件位置**：项目根目录下会自动创建 `log` 文件夹，存放日志文件

2. **Mapper XML位置**：手写SQL放在 `source/` 目录下，符合项目规范

3. **DTO使用规范**：所有Controller接口必须使用DTO，不允许使用Map接收参数

4. **跨域配置**：已配置允许所有来源跨域访问，生产环境需根据实际情况调整

5. **JWT密钥**：生产环境请修改 `application.yml` 中的 `jwt.secret`

## 开发建议

1. 使用IDEA开发，安装Lombok插件
2. 使用Postman测试接口
3. 日志文件会自动在项目根目录的 `log/` 文件夹生成
4. 建议使用Docker部署MySQL、Redis、RabbitMQ

## 版本信息

- **版本号**：1.0.0
- **开发时间**：2024-03-27
- **JDK版本**：11
- **Spring Boot版本**：2.7.18
