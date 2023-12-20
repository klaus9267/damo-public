package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.PrincipalDetails;
import com.damo.server.domain.common.pagination.CustomSchedulePage;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.ScheduleAmount;
import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/total-amounts")
    @Operation(summary = "거래 총액 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleAmount> readTotalAmounts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final ScheduleAmount amount = scheduleService.readTotalAmounts(principalDetails.getUser().getId());
        return ResponseEntity.ok(amount);
    }

    @GetMapping("{scheduleId}")
    @Operation(summary = "스케줄 단건 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<ScheduleDto> readSchedule(
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final ScheduleDto schedule = scheduleService.readSchedule(scheduleId, principalDetails.getUser().getId());
        return ResponseEntity.ok(schedule);
    }

    @GetMapping
    @Operation(summary = "스케줄 타입별 목록 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @PageableAsQueryParam
    public ResponseEntity<CustomSchedulePage> readScheduleList(
            @Valid @Parameter(hidden = true) final SchedulePaginationParam paginationParam,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        final CustomSchedulePage schedule = scheduleService.readScheduleList(paginationParam, principalDetails.getUser().getId());
        return ResponseEntity.ok(schedule);
    }

    @PatchMapping("{scheduleId}")
    @Operation(summary = "스케줄 수정", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchScheduleById(
            @RequestBody final RequestScheduleDto scheduleDto,
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        scheduleService.patchScheduleById(scheduleDto, scheduleId, principalDetails.getUser().getId());
    }

    @DeleteMapping("{scheduleId}")
    @Operation(summary = "스케줄 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeScheduleById(
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        scheduleService.removeScheduleById(scheduleId, principalDetails.getUser().getId());
    }
}
