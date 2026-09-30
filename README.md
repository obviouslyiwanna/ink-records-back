# Ink Records Backend

墨记的 Java REST API，负责记录 CRUD、HTML 清理、H2 持久化和本地图片上传。

## 开发环境

- JDK 17+
- Maven 3.6.3+

## 启动

```powershell
mvn spring-boot:run
```

服务默认运行于 `http://localhost:8080`。

## 测试

```powershell
mvn test
```

## API

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| GET | `/api/records` | 查询全部记录 |
| GET | `/api/records?category=WORK` | 按分类查询 |
| GET | `/api/records/{id}` | 查询详情 |
| POST | `/api/records` | 新建记录 |
| PUT | `/api/records/{id}` | 更新记录 |
| DELETE | `/api/records/{id}` | 删除记录 |
| POST | `/api/uploads` | 上传图片 |

## 部署配置

Docker 镜像默认监听 8080。可通过环境变量覆盖 Spring 配置：

- `INK_RECORDS_ALLOWED_ORIGINS`：允许访问 API 的前端域名，多个域名用逗号分隔
- `INK_RECORDS_UPLOAD_DIR`：图片目录，默认 `./data/uploads`
- `SPRING_DATASOURCE_URL`：数据库连接地址
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

使用当前 H2 与本地图片方案部署时，宿主平台必须给 `/app/data` 挂载持久卷，否则重新部署会丢失数据和图片。
