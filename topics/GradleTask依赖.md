### GradleTask依赖

Gradle task与task之间用 depends 来引入依赖

```gradle
task init {
    doLast {
        println " This is init"
    }
}

task B(dependsOn: 'init'){
    doLast {
        println "This is B"
    }
}

task A(dependsOn: 'init') {
    doLast {
        println "This is A"
    }
}

```

对多个task依赖 `dependsOn: ['A', 'B']`
```gradle
task C (dependsOn: ['A', 'B']){
    doLast {
        println " This is c"
    }
}
```

如果依赖的task之间没能构建依赖，例如 C依赖A，B, A,B之间没有声明依赖，则会按照**task name的字符串先后顺序执行**。
即:

```console
:init
 This is init
:ssA
This is A
:ssB
This is B
:C
 This is c


```
