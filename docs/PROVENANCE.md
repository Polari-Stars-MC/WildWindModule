# 项目来源与整理范围

本仓库于 2026-10-23 从 WildWindModule 的当前工作区建立独立源码快照，包含已提交代码及当时尚未提交的功能实现。

- 上游项目：https://github.com/Polari-Stars-MC/WildWindModule
- 上游分支：`26.2`
- 已合并的上游提交：`26ad2da288d7018a1eb39d5bfbf30b2ad0a1520c`
- 整理前本地基线：`39e327eca50f258c645097332d76319c9553e8d4`
- 新增内容：统一结构生成、材料和生物、沉溺／窒息、熔炼、骷髅与耕地规则、窑炉、杜鹃木功能方块、延展之箭及测试。

## 保留内容

源码、自动测试及测试夹具、游戏资源、Blockbench 模型源文件、Gradle Wrapper、构建配置、GitHub Actions、功能文档、需求说明与原有许可证。泥猪源模型和七种物品图标已经包含在资源目录，不重复复制外部素材目录。

## 排除内容

本地 IDE 配置、Gradle 缓存、数据生成缓存、构建输出、运行日志、测试世界、JDK 与辅助工具、安装 ZIP/JAR 以及父目录的飞书抓取临时文件。Gradle Wrapper JAR 是构建所需的例外，予以保留。

2026-10-23 的独立初始快照没有复制原仓库的 `.git` 历史、凭据或远端配置。2026-10-24 的回传通过双亲合并连接目标仓库历史与独立快照历史，来源基线和兼容性限制见 [上游同步记录](UPSTREAM_SYNC.md)。旧项目保留，可用于查询初始快照之前的本地开发历史。

## 上游贡献署名

整理前可达提交记录中的作者名称如下（保留原有各源文件署名）：

- Assistant
- CarrotLegend
- Codex
- Mon#Landis
- MonLandis
- baka4n
- cuccuc377-dev
- mdjfnfn
- 刘冬煜

## 许可证

保留 `LICENSE`、`LICENSE_CODE`、`LICENSE_ASSETS`，未变更许可声明。2026-10-24 将构建元数据中的 `All Rights Reserved` 更正为现有仓库声明的 `GPL-3.0-only AND CC-BY-SA-4.0`，使 JAR 元数据与许可证一致。
