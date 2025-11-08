package com.codedev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main class for the Jenkins demo application.
 */
@SpringBootApplication
@RestController
public class JenkinsDemoApplication {

    /**
     * Test endpoint for Jenkins.
     *
     * @return greeting string
     */
    @GetMapping
    public String testJenkins() {
        return "Hello Jenkins";
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(JenkinsDemoApplication.class, args);
    }
}
