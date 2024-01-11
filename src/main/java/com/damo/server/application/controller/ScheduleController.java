package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.transaction.TransactionOperationWithBody;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import com.damo.server.domain.transaction.dto.TransactionDto;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // swagger 추가 예정
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSchedule(
            @Valid @RequestBody final RequestCreateScheduleDto scheduleDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        scheduleWriteService.addSchedule(scheduleDto, user.getId());
    }

    // swagger 추가 예정
    @GetMapping("{scheduleId}")
    public ResponseEntity<ScheduleDto> readSchedule(
            @PathVariable("scheduleId") final Long scheduleId,
            @AuthenticationPrincipal final UserDto user
    ) {
        final ScheduleDto scheduleDto = scheduleReadServices.readSchedule(scheduleId, user.getId());
        return ResponseEntity.ok(scheduleDto);
    }
}
