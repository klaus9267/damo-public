package com.damo.server.domain.schedule.service;

import com.damo.server.domain.schedule.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleReadService {
    private final ScheduleRepository scheduleRepository;
}
