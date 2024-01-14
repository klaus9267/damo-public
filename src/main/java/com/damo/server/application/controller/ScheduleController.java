package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.schedule.CreateScheduleOperationWithBody;
import com.damo.server.application.controller.operation.schedule.ScheduleOperationWithBody;
import com.damo.server.application.controller.operation.schedule.ScheduleOperationWithNoBody;
import com.damo.server.application.controller.operation.schedule.UpdateScheduleOperationWithBody;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void addSchedule(
            @Valid @RequestBody final RequestCreateScheduleDto scheduleDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        scheduleWriteService.addSchedule(scheduleDto, user.getId());
    }

    @GetMapping("{scheduleId}")
    @ScheduleOperationWithBody(summary = "일정 단건 조회")
    public ResponseEntity<ScheduleDto> readSchedule(
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal final UserDto user
    ) {
        final ScheduleDto scheduleDto = scheduleReadServices.readSchedule(scheduleId, user.getId());
        return ResponseEntity.ok(scheduleDto);
    }

    // swagger 추가 예정
    @PatchMapping("{scheduleId}")
    @UpdateScheduleOperationWithBody(summary = "일정 수정", description = "내역 생략 가능")
    public void patchScheduleById(
            @PathVariable("scheduleId") final Long scheduleId,
            @Valid @RequestBody final RequestUpdateScheduleDto scheduleDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        scheduleWriteService.patchScheduleById(scheduleDto, scheduleId, user.getId());
    }

    @DeleteMapping("{scheduleId}")
    @ScheduleOperationWithNoBody(summary = "일정 삭제")
    public void removeScheduleById(
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal final UserDto user
    ) {
        scheduleWriteService.removeScheduleById(scheduleId, user.getId());
    }
}
