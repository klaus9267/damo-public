package com.damo.server.domain.schedule;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.CustomSchedulePage;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void save(final RequestScheduleDto scheduleDto) {
        if (scheduleRepository.existsByDateAndEventAndPersonId(scheduleDto.date(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        scheduleRepository.save(Schedule.from(scheduleDto));
    }

    public ScheduleDto readSchedule(final Long scheduleId) {
        // TODO: security로 userId 받으면 조회 시 조건문에 userId 추가
        return scheduleRepository.findOne(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
    }

    public CustomSchedulePage readScheduleList(final SchedulePaginationParam schedulePaginationParam, final Long userId) {
        Page<ScheduleDto> page = scheduleRepository.findAllByUserId(schedulePaginationParam.toPageable(), userId, schedulePaginationParam.getStartedAt(), schedulePaginationParam.getEndedAt());
//        List<ScheduleDto> scheduleDtos = transaction.equals(ScheduleTransaction.TOTAL) ?
//                                         page.getContent() :
//                                         transaction.equals(ScheduleTransaction.RECEIVING) ?
//                                         page.getContent().stream().filter(dto -> dto.getTransaction().equals(ScheduleTransaction.RECEIVING)).toList() :
//                                         page.getContent().stream().filter(dto -> dto.getTransaction().equals(ScheduleTransaction.GIVING)).toList();
        return new CustomSchedulePage(page.getContent(), schedulePaginationParam.toPageable() , scheduleRepository.findTotalAmount(userId), null,null);
    }

    @Transactional
    public void patchScheduleById(final RequestScheduleDto scheduleDto, final Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        if (!schedule.getPerson().getUser().getId().equals(scheduleId)) {
            throw new BadRequestException("다른 사용자의 스케줄");
        }
        schedule.changeInfo(scheduleDto);
    }

    public void removeScheduleById(final Long scheduleId) {
        this.readSchedule(scheduleId);
        // TODO: security로 userId 받으면 같은 유저인지 판단 조건 추가
        scheduleRepository.deleteById(scheduleId);
    }
}
