package com.damo.server.domain.schedule;

import com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto;
import com.damo.server.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * `ScheduleRepository`는 `Schedule` 엔터티에 대한 데이터베이스 액세스를 제공하는 JpaRepository입니다.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  /**
   * 주어진 행사, 행사 날짜, 사용자 ID에 해당하는 일정을 반환합니다.
   *
   * @param event     조회할 행사 이름
   * @param eventDate 조회할 행사 날짜
   * @param userId    조회할 사용자의 ID
   * @return 주어진 조건에 해당하는 일정(존재하지 않을 경우 빈 Optional)
   */
  Optional<Schedule> findByEventAndEventDateAndUserId(final String event, final LocalDateTime eventDate, final Long userId);
  
  /**
   * 주어진 일정 ID ,사용자 ID에 해당하는 일정을 반환합니다.
   *
   * @param id     조회할 일정 ID
   * @param userId 조회할 사용자 ID
   * @return 주어진 조건에 해당하는 일정(존재하지 않을 경우 빈 Optional)
   */
  Optional<Schedule> findByIdAndUserId(final Long id, final Long userId);
  
  /**
   * 주어진 사용자 ID, 기간에 해당하는 모든 일정을 포함하는 페이지를 반환합니다.
   *
   * @param pageable 페이지 정보
   * @param userId   조회할 사용자 ID
   * @param year     조회할 월
   * @param month    조회할 년도
   * @param keyword  검색 키워드 (행사와 일치해야 함)
   * @return 페이지에 포함된 일정 정보
   */
  @Query("""
         SELECT new com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto(s,t)
         FROM Schedule s
              LEFT JOIN FETCH  s.transaction t
              LEFT JOIN FETCH  t.person p
         WHERE p.user.id = :userId
              AND (:month IS NULL OR FUNCTION('MONTH', s.eventDate) = :month)
              AND (:year IS NULL OR FUNCTION('YEAR', s.eventDate) = :year)
              AND  (:keyword IS NULL OR s.event LIKE :keyword)
         """)
  Page<ScheduleWithTransactionDto> findAllScheduleByEventDate(
      final Pageable pageable,
      @Param("userId") final Long userId,
      @Param("year") final Integer year,
      @Param("month") final Integer month,
      @Param("keyword") final String keyword
  );
}
