package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.schedule.CreateScheduleOperationWithBody;
import com.damo.server.application.controller.operation.schedule.ScheduleOperationWithBody;
import com.damo.server.application.controller.operation.schedule.ScheduleOperationWithNoBody;
import com.damo.server.application.controller.operation.schedule.UpdateScheduleOperationWithBody;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "SCHEDULE")
public class ScheduleController {
    private final ScheduleWriteService scheduleWriteService;
    private final ScheduleReadService scheduleReadServices;

    @PostMapping
    @CreateScheduleOperationWithBody(summary = "일정 추가")
    public void addSchedule(@Valid @RequestBody final RequestCreateScheduleDto scheduleDto) {
        scheduleWriteService.addSchedule(scheduleDto);
    }

    @GetMapping("{scheduleId}")
    @ScheduleOperationWithBody(summary = "일정 단건 조회")
    public ResponseEntity<ScheduleDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
        final ScheduleDto scheduleDto = scheduleReadServices.readSchedule(scheduleId);
        return ResponseEntity.ok(scheduleDto);
    }

    @GetMapping
    @ScheduleOperationWithBody(summary = "년,월별 일정 리스트 조회")
    public ResponseEntity<SchedulePaginationResponseDto> readScheduleListByDate(@ParameterObject final SchedulePaginationParam paginationParam) {
        final SchedulePaginationResponseDto scheduleDto = scheduleReadServices.readScheduleByEventDate(paginationParam);
        return ResponseEntity.ok(scheduleDto);
    }

    @PatchMapping("{scheduleId}")
    @UpdateScheduleOperationWithBody(summary = "일정 수정", description = "내역 생략 가능")
    public void patchScheduleById(
            @PathVariable("scheduleId") final Long scheduleId,
            @Valid @RequestBody final RequestUpdateScheduleDto scheduleDto
    ) {
        scheduleWriteService.patchScheduleById(scheduleDto, scheduleId);
    }

    @DeleteMapping("{scheduleId}")
    @ScheduleOperationWithNoBody(summary = "일정 삭제")
    public void removeScheduleById(@PathVariable("scheduleId") final Long scheduleId) {
        scheduleWriteService.removeScheduleById(scheduleId);
    }
}
