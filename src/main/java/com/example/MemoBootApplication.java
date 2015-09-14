package com.example;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Memos Boot application configuration and main class.
 */
@SpringBootApplication
@EnableSwagger
@EnableAutoConfiguration
public class MemoBootApplication extends SpringBootServletInitializer {

    /**
     * Main Method to start Spring Boot application.
     *
     * @param args String Main Args
     */
    public static void main(final String[] args) {
        SpringApplication.run(MemoBootApplication.class, args);
    }

    /**
     * Sample bootApplication method.
     */
    public void bootApplication() {
    }
}
