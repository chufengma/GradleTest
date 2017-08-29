package com.onefengma.hello;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class HelloPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.task('hello') {
            doLast {
            	if (false) {
            		println "HAHAHA false"
            	} else {
            		println "HAHAHA true"            		
            	}

                println "Hello from the GreetingPlugin"
            }
        }

        project.configurations {
            hello
        }

        project.ext.helloName = ""

        

    }
    
}