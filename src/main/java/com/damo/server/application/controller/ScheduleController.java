package com.damo.server.application.controller;

import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDto> addPerson(@RequestBody final RequestScheduleDto scheduleDto) {
        // TODO: personId는 security에서 제공하는 데이터로 변경
        return  ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(scheduleDto));
    }

    @GetMapping("{scheduleId}")
    @Operation(summary = "스케줄 단건 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
        final ScheduleDto schedule = scheduleService.readSchedule(scheduleId);
        return ResponseEntity.ok(schedule);
    }
}
