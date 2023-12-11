package com.damo.server.application.controller;

import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("{scheduleId}")
    @Operation(summary = "스케줄 단건 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
        final ScheduleDto schedule = scheduleService.readSchedule(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("{scheduleId}")
    @Operation(summary = "스케줄 삭제")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> removeScheduleById(@PathVariable("scheduleId") final Long scheduleId) {
        scheduleService.removeScheduleById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
