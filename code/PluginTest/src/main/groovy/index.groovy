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