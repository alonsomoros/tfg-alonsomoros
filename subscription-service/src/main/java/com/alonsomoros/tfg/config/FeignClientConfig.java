package com.alonsomoros.tfg.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.alonsomoros.tfg")
public class FeignClientConfig {
}
