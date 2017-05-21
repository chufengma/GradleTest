### gradle核心对象与声明周期.md

1.  Gradle, Project, Setting

Gradle : Gradle相关的版本，路径等信息
Project : 跟build.gradle对应，标识一个构建项目
Setting : 跟setting.gradle对应，项目及子项目的配置

2. 生命周期
- 创建一个Setting对象，如果有setting.gradle脚本则执行并绑定Setting对象。
- 通过Setting对象创建Project树（Project和Project子项目, 和build.gradle一一对应）
- 执行每一个Project(build.gradle)，执行顺序可以通过Project.evaluationDependsOn(java.lang.String)，Project.evaluationDependsOnChildren() 改变
