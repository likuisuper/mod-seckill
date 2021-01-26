package com.cxylk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ModSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModSeckillApplication.class, args);
    }

}
