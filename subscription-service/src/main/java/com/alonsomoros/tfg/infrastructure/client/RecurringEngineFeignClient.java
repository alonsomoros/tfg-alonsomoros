package com.alonsomoros.tfg.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.alonsomoros.tfg.infrastructure.client.feign.dto.TokenRequestDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "recurring-engine", url = "http://localhost:8081/recurring")
public interface RecurringEngineFeignClient {

    @PostMapping("/tokens")
    void sendToken(@RequestBody TokenRequestDto request);

}
