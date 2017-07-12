### gradle核心对象与声明周期.md

#### 1.  Gradle, Project, Setting

Gradle : Gradle相关的版本，路径等信息
Project : 跟build.gradle对应，标识一个构建项目
Setting : 跟setting.gradle对应，项目及子项目的配置

#### 2. 生命周期

##### 1.初始化
  创建一个Setting对象，如果有setting.gradle脚本则执行并绑定Setting对象。
  查找settings.gradle方式如下：
  
- 在当前文件树中查找master目录，并从节点往上查找settings.gradle, 如图先查找相临的master目录，如果不存在再查找上级目录中的master.

```
               /-- b (build.gradle 所在目录)
            a  -- master (会优先查找这里) 
          /    \-- d
      root  
          \ 
            master
```
   
 - 如果第一步没有找到，则查找父目录中是否有settings.gradle
 - 如果没有找到则当成单工程处理
 - 找到settings.gradle之后，解析当前工程是否是多工程还是单工程
 
#### 2.配置 
     通过Setting对象创建Project树（Project和Project子项目, 和build.gradle一一对应）
     
#### 3.执行 
    执行每一个Project(build.gradle)，执行顺序可以通过Project.evaluationDependsOn(java.lang.String)，Project.evaluationDependsOnChildren() 改变
