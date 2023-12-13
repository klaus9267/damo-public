package com.damo.server.domain.schedule;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.person.PersonRepository;
import com.damo.server.domain.schedule.dto.RequestScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleDto;
import com.damo.server.domain.schedule.dto.ScheduleWithPersonDto;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.schedule.entity.ScheduleTransaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PersonRepository personRepository;

    @Transactional
    public ScheduleDto save(final RequestScheduleDto scheduleDto) {
        if (scheduleRepository.existsByDateAndEventAndPersonId(scheduleDto.date(), scheduleDto.event(), scheduleDto.personId())) {
            throw new BadRequestException("스케줄 내에서 동일한 기록이 존재");
        }
        //TODO: return 시 person 출력 유무 협의 예정
        return ScheduleMapper.toDto(scheduleRepository.save(ScheduleMapper.toEntity(scheduleDto)));
    }

    public ScheduleDto readSchedule(final Long scheduleId) {
        // TODO: security로 userId 받으면 조회 시 조건문에 userId 추가
        return scheduleRepository.findOne(scheduleId).orElseThrow(() -> new NotFoundException("조회할 대상을 찾을 수 없음"));
    }

    public Page<ScheduleWithPersonDto> readScheduleList(final Pageable pageable, final Long userId, final String type) {
        // TODO: totalAmount 관련 협의 필요
        return scheduleRepository.findAllWithTotalAmount(pageable, ScheduleTransaction.valueOf(type), userId);
    }

    @Transactional
    public ScheduleDto patchScheduleById(final RequestScheduleDto scheduleDto, final Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        // TODO: security로 userId 받으면 같은 유저인지 판단 조건 추가

        schedule.setDate(scheduleDto.date() != null ? scheduleDto.date() : schedule.getDate());
        schedule.setAmount(scheduleDto.amount() != null ? scheduleDto.amount() : schedule.getAmount());
        schedule.setMemo(scheduleDto.memo() != null ? scheduleDto.memo() : schedule.getMemo());
        schedule.setEvent(scheduleDto.event() != null ? scheduleDto.event() : schedule.getEvent());
        schedule.setStatus(scheduleDto.status() != null ? ScheduleStatus.valueOf(scheduleDto.status()) : schedule.getStatus());
        schedule.setTransaction(scheduleDto.transaction() != null ? ScheduleTransaction.valueOf(scheduleDto.transaction()) : schedule.getTransaction());

        return ScheduleMapper.toDto(schedule);
    }

    public void removeScheduleById(final Long scheduleId) {
        this.readSchedule(scheduleId);
        // TODO: security로 userId 받으면 같은 유저인지 판단 조건 추가
        scheduleRepository.deleteById(scheduleId);
    }
}
