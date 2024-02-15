package com.damo.server.domain.schedule.dto;

import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * {@code ScheduleDto}는 일정 정보를 포함한 DTO 클래스입니다.
 */
@Builder
@AllArgsConstructor
@Getter
public class ScheduleDto {
  private final Long id;
  private final String event;
  private final LocalDateTime eventDate;
  private final String memo;
  private final ScheduleStatus status;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  
  /**
   * 내부 생성자로, 일정 에너ㅣ에서 데이터를 이용해 DTO를 생성합니다.
   *
   * @param schedule 일정 에너티
   */
  private ScheduleDto(final Schedule schedule) {
    this.id = schedule.getId();
    this.event = schedule.getEvent();
    this.eventDate = schedule.getEventDate();
    this.memo = schedule.getMemo();
    this.status = schedule.getStatus();
    this.createdAt = schedule.getCreatedAt();
    this.updatedAt = schedule.getUpdatedAt();
  }
  
  /**
   * 일정 엔터티에서 'ScheduleDto'로 변환합니다.
   *
   * @param schedule 일정 엔터티
   * @return 'ScheduleDto' 객체
   */
  public static ScheduleDto from(final Schedule schedule) {
    return new ScheduleDto(schedule);
  }
}
