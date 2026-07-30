package com.alonsomoros.tfg.infrastructure.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IPlanService;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
@RequestMapping
@RestController
@Tag(name = "Plan Controller", description = "Endpoints for plan management")
public class PlanController {

    private final IPlanService planService;

    @Operation(summary = "Get all plans", description = "Retrieves a list of all active plans")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plans retrieved successfully", 
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlanResponseDto.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(EndpointConstants.GET_PLANS)
    public List<PlanResponseDto> getPlans() {
        log.info("Received request to get all plans.");
        List<PlanResponseDto> response = planService.getPlans();
        log.info("Plans retrieved successfully.");
        return response;
    }
}
