package com.lishui.tourism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 丽水智慧旅游管理系统启动类
 */
@SpringBootApplication
public class LishuiTourismApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LishuiTourismApplication.class, args);
        System.out.println("\n=================================================");
        System.out.println("    丽水智慧旅游管理系统启动成功！");
        System.out.println("    Swagger文档地址: http://localhost:8081/api/swagger-ui.html");
        System.out.println("=================================================\n");
    }
}
