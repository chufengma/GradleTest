
## Gradle 与 groovy DSL

groovy 可以很方便的实现类似如下这样的DSL, 简洁，清晰，同时高度灵活。理解这样的DSL的实现，其实包含俩个部分：**表象的闭包声明** + **实际的闭包实现***。

```
dependencies {
    compile 'org.greenrobot:eventbus:3.0.0'
    provided 'org.greenrobot:eventbus:3.0.0'
    apt 'org.greenrobot:eventbus-annotation-processor:3.0.1'
}
```

### 闭包声明

groovy定义一个函数，参数是一个Closure, 然后通过groovy支持的链式调用可以在形式上做到示例中的调用
```
def dependencies(Closure cl) {
    // balabala
}
...

dependencies {
    // balabala
}

```

### 功能实现

写Java程序我们经常定义一个类来完成某件业务, groovy提供 rehydrate 来绑定一个一个闭包和一个具体的对象，这样一来可以把上面声明的闭包映射到具体的实现对象上去，从而实现DSL。



```
class DependenciesImpl {
    void compile(String dep) {
        println "compile added : $dep" 
    }
    void provided(String dep) {
        println "provided added : $dep"
    }
    void apt(String dep) {
        println "apt added: $dep"
    }
}

...

def dependencies(Closure cl) {
    def dep = new DependenciesImpl()
    def code = cl.rehydrate(dep, this, this)
    code.resolveStrategy = Closure.DELEGATE_ONLY
    code()
}

...
dependencies {
    compile 'org.greenrobot:eventbus:3.0.0'
    provided 'org.greenrobot:eventbus:3.0.0'
    apt 'org.greenrobot:eventbus-annotation-processor:3.0.1'
}

```

### 参考
[grrovy DSL](http://groovy-lang.org/closures.html#_varargs)

