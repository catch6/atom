# Atom Build

Atom 内部构建父 POM，承载发布相关 profile（GPG 签名、源码/Javadoc 打包、发布到 Maven Central）。

## 说明

`atom-build` 是 Atom 仓库内部各功能模块的直接父 POM，继承自 `atom-parent`。它在 `atom-parent` 的基础上增加了 `release` profile，用于执行完整的发布流程。

**仅供 atom 仓库内部模块继承，下游项目请使用 `atom-parent` 或 `atom-dependencies`。**

### release profile 包含的插件

| 插件 | 作用 |
|------|------|
| `maven-compiler-plugin` | Java 编译 |
| `maven-source-plugin` | 打包源码 JAR |
| `maven-javadoc-plugin` | 生成 Javadoc JAR |
| `flatten-maven-plugin` | 解析 `${revision}` CI 友好版本 |
| `central-publishing-maven-plugin` | 发布到 Maven Central |
| `maven-gpg-plugin` | GPG 签名 |

### 发布命令

```bash
mvn clean deploy -P release
```

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
