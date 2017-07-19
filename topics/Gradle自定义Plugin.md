## Gradle自定义Plugin

在使用Gradle构建Android的过程中，gradle脚本中经常会使用到其它 plugin, 比如 maven-plugin, apt-plugin等等, 这些plugin是对gradle已有功能的扩展。那么我们如何自定义plugin 并且可以通过
gradle dependencies优雅的引入呢？

## 自定义Plugin

新建一个简单的Java工程，在build.gradle中加入：
```
apply plugin: TestPlugin

test {
    message = "Hell good";
}

class TestPlugin implements Plugin<Project> {
    void apply(Project project) {

        project.extensions.create("test", TestPluginExtension)

        project.task('hello') {
            doLast {
                println project.test.message
            }
        }
    }
}

class TestPluginExtension {
    def String message = 'Hello from GreetingPlugin'
}
```

类TestPlugin实现Plugin，并实现apply方法。通过 project.extensions.create("test", TestPluginExtension) 添加名为test的 Extension, 并创建task hello。这样当前脚本中 就可以引入 TestPlugin 并执行 test脚本块。

## 独立工程编译Plugin 上传到本地maven仓库

通过在同一个脚本中创建Plugin并执行可以做到添加plugin扩展，如果要在dependencies中引入自定义Plugin则需要建立独立的Groovy工程 并且打包上传到仓库
```
|____build.gradle
|____src
| |____main
| | |____groovy
| | | |____index.groovy
| | |____resources
| | | |____META-INF
| | | | |____gradle-plugins
| | | | | |____GreetingPlugin.properties
```

1. 创建index.groovy
```
// index.groovy
package com.onefengma.gradle;
import org.gradle.api.Project;
import org.gradle.api.Plugin;

class GreetingPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Add the 'greeting' extension object
        project.extensions.create("greeting", GreetingPluginExtension)
        // Add a task that uses the configuration
        project.task('hello') {
            doLast {
                println project.greeting.message
            }
        }
    }
}

class GreetingPluginExtension {
    def String message = 'Hello from GreetingPlugin'
}
```
2. 创建 src/main/resources/META-INF/gradle-plugins/GreetingPlugin.properties
其中 GreetingPlugin.properties 文件名就是 plugin ID

```
// GreetingPlugin.properties
implementation-class=com.onefengma.gradle.GreetingPlugin
```

3. 独立工程中的build.gradle 编译并打包上传到本地仓库

执行 gradle build, gradle publish

```
// build.gradle

apply plugin: 'java'
apply plugin: 'groovy'  // groovy plugin

dependencies {
    compile gradleApi()
    compile localGroovy()
}

apply plugin: 'maven-publish'

group = "com.onefengma.gradle"
version = "0.1"
def mavenLocal = "/Users/dev/local_maven/repo";
def name = "PluginTest";

task sourceJar(type: Jar) {
    from sourceSets.main.allJava
    classifier "sources"
}

publishing {
    publications {
        archives(MavenPublication) {
            groupId project.getGroup()
            artifactId "$name"
            version project.getVersion()
            artifact(project.getRootDir().absolutePath + "/build/libs/$name-$version" + ".jar") {
                extension 'jar'
            }
        }
    }
    repositories {
        maven {
            url "$mavenLocal"
        }
    }
}
```


## 在其他工程中使用
在独立的java工程中 使用刚刚提交的 GreetingPlugin
```
// build.gradle
buildscript {
    repositories {
        jcenter()
        maven {
            url "/Users/dev/local_maven/repo"
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.0'
        classpath 'com.onefengma.gradle:PluginTest:0.1'
    }
}

apply plugin: 'GreetingPlugin'

greeting {
    message = "Hell good";
}

```
执行plugin中加入的task: gradle hello
```
$: gradle hello

:hello
Hell good

BUILD SUCCESSFUL

Total time: 7.116 secs

```

## 参考

- https://docs.gradle.org/current/userguide/custom_plugins.html
- http://blog.csdn.net/eclipsexys/article/details/50973205
