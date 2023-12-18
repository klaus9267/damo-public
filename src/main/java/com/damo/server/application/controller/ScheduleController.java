package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.PrincipalDetails;
import com.damo.server.domain.common.pagination.CustomSchedulePage;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "SCHEDULE")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "스케줄 생성", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSchedule(@RequestBody final RequestScheduleDto scheduleDto) {
        scheduleService.save(scheduleDto);
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
    public ResponseEntity<CustomSchedulePage> readScheduleList(
            @Valid final SchedulePaginationParam paginationParam,
            @Parameter(name = "transaction", description = "조회할 스케줄 종류", example = "RECEIVING")
            @RequestParam(required = true) final ScheduleTransaction transaction,
            @Parameter(name = "startedAt", description = "조회 시작 날짜", example = "2023-12-18")
            @RequestParam(required = false) final LocalDateTime startedAt,
            @Parameter(name = "endedAt", description = "조회 종료 날짜", example = "2023-12-18")
            @RequestParam(required = false) final LocalDateTime endedAt,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final CustomSchedulePage schedule = scheduleService.readScheduleList(paginationParam.toPageable(), principalDetails.getUser().getId(), transaction, startedAt, endedAt);
        return ResponseEntity.ok(schedule);
    }

    @PatchMapping("{scheduleId}")
    @Operation(summary = "스케줄 수정", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchScheduleById(
            @RequestBody final RequestScheduleDto scheduleDto,
            @PathVariable("scheduleId") final Long scheduleId
    ) {
        scheduleService.patchScheduleById(scheduleDto, scheduleId);
    }

    @DeleteMapping("{scheduleId}")
    @Operation(summary = "스케줄 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeScheduleById(@PathVariable("scheduleId") final Long scheduleId) {
        scheduleService.removeScheduleById(scheduleId);
    }
}
