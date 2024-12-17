package org.example.course_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CourseServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseServerApplication.class, args);
    }
}
