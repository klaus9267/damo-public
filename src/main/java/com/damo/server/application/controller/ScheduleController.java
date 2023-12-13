package com.damo.server.application.controller;

import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleWithPersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
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
    @Operation(summary = "스케줄 생성")
    @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleDto> save(@RequestBody final RequestScheduleDto scheduleDto) {
        // TODO: personId는 security에서 제공하는 데이터로 변경
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(scheduleDto));
    }

    @GetMapping("{scheduleId}")
    @Operation(summary = "스케줄 단건 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
        final ScheduleDto schedule = scheduleService.readSchedule(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping
    @Operation(summary = "스케줄 타입별 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<Page<ScheduleWithPersonDto>> readScheduleList(
            @Valid final SchedulePaginationParam paginationParam,
            @Parameter(name = "조회할 스케줄 종류", example = "RECEIVING")
            @RequestParam(required = true)
            @Length(max = 20) final String transaction) {
        final Page<ScheduleWithPersonDto> schedule = scheduleService.readScheduleList(paginationParam.toPageable(), 1L, transaction);
        return ResponseEntity.ok(schedule);
    }

    @PatchMapping("{scheduleId}")
    @Operation(summary = "스케줄 수정")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<?> patchScheduleById(
            @RequestBody final RequestScheduleDto scheduleDto,
            @PathVariable("scheduleId") final Long scheduleId) {
        return ResponseEntity.ok(scheduleService.patchScheduleById(scheduleDto, scheduleId));
    }

    @DeleteMapping("{scheduleId}")
    @Operation(summary = "스케줄 삭제", description = "응답 없음")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> removeScheduleById(@PathVariable("scheduleId") final Long scheduleId) {
        scheduleService.removeScheduleById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
