package com.damo.server.application.controller;

import com.damo.server.domain.schedule.ScheduleService;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("{scheduleId}")
    public ResponseEntity<ScheduleDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
        final ScheduleDto schedule = scheduleService.readSchedule(scheduleId);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
        // return ResponseEntity.ok(schedule);
        // ok는 위와 같은 형식도 있는데 저건 어떤지 다른 메서드는 url같은 추가적인 정보가 필요하긴한데
        // ok는 body만 있으면 되서 저렇게하면 깔끔할 것 같음
    }
}
