<h1 align="center">Atom</h1>

<p align="center">
	<a target="_blank" href="https://central.sonatype.com/artifact/net.wenzuo/atom">
		<img src="https://img.shields.io/maven-central/v/net.wenzuo/atom.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-17+-blue.svg" />
	</a>
</p>

Atom 是一个基于 SpringBoot 和 SpringCloud 的企业级常用组件封装库，提供了丰富的功能和强大的扩展性，可以帮助开发者快速构建高效、稳定的应用。

## 模块

## 快速开始

在 pom.xml 中指定 parent

```xml

<parent>
	<groupId>net.wenzuo</groupId>
	<artifactId>atom</artifactId>
	<version>2.0.0</version>
	<relativePath/>
</parent>
```

根据需要的模块引入相关依赖

```xml

<dependency>
	<groupId>net.wenzuo</groupId>
	<artifactId>atom-spring-boot-starter-web</artifactId>
</dependency>
```

## 配置项

各个模块的配置项，可以在 application.yml 中覆盖

```yaml

```

## 一些建议

建议在生产环境中在网关侧配置CORS并关闭服务的CORS配置
建议在生产环境关闭Swagger及其相关配置

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false

atom:
  web:
    cors:
      enabled: false
```