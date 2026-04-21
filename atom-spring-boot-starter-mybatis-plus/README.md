# Atom Spring Boot Starter MyBatis Plus

Atom MyBatis Plus 数据库访问模块，提供分页插件、创建/更新时间自动填充、类型处理器和分页转换工具。

## 功能特性

- **分页插件** — 自动配置 MyBatis Plus 分页拦截器
- **自动填充** — 自动填充 createTime / updateTime 字段
- **类型处理器** — 内置 BigDecimal、Integer、LocalDateTime 类型处理器
- **分页转换** — PageUtils 工具类，MyBatis Plus Page 与 Atom PageVO 无缝转换

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-mybatis-plus</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  mybatis-plus:
    enabled: true
    pagination: true          # 启用分页插件
    auto-fill: false          # 启用创建/更新时间自动填充
    create-time-field: createTime  # 创建时间字段名（entity 属性名）
    update-time-field: updateTime  # 更新时间字段名（entity 属性名）
```

### 使用示例

#### 分页查询

```java
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public PageVO<UserVO> page(UserQuery query) {
        Page<User> page = new Page<>(query.getPage(), query.getSize());
        userMapper.selectPage(page, null);
        return PageUtils.toPageVO(page, UserVO.class);
    }
}
```

#### 自动填充

启用 `auto-fill: true` 后，Entity 中的 createTime / updateTime 字段在插入和更新时自动填充：

```java
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    // 插入时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 插入和更新时自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.mybatis-plus.enabled` | Boolean | `true` | 是否启用模块 |
| `atom.mybatis-plus.pagination` | Boolean | `true` | 是否启用分页插件 |
| `atom.mybatis-plus.auto-fill` | Boolean | `false` | 是否启用时间自动填充 |
| `atom.mybatis-plus.create-time-field` | String | `createTime` | 创建时间字段名 |
| `atom.mybatis-plus.update-time-field` | String | `updateTime` | 更新时间字段名 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
