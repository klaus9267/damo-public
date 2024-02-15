package com.damo.server.domain.bulk;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@code ScheduleBulk} 클래스는 데이터베이스와 관련된 일괄 작업을 수행합니다.
 * 일정을 지우거나 무작위 데이터를 사용하여 일괄 삽입하는 메서드를 제공합니다.
 */
@Repository
@RequiredArgsConstructor
public class ScheduleBulk {
  private final JdbcTemplate jdbcTemplate;
  private final SecurityUserUtil securityUserUtil;
  
  /**
   * 데이터베이스에서 모든 일정을 삭제합니다.
   */
  @Transactional
  public void clear() {
    final String sql = "DELETE FROM schedules";
    jdbcTemplate.execute(sql);
  }
  
  /**
   * 무작위 데이터를 사용하여 일정을 일괄 삽입하고 현재 사용자와 연결합니다.
   *
   * @param batchSize 무작위로 삽입할 일정의 배치 크기입니다.
   * @param start     무작위 이벤트 날짜를 생성하는 데 사용되는 시작 날짜 범위입니다.
   * @param end       무작위 이벤트 날짜를 생성하는 데 사용되는 종료 날짜 범위입니다.
   */
  @Transactional
  public void bulkInsertWithSchedule(
      final Integer batchSize,
      final LocalDateTime start,
      final LocalDateTime end
  ) {
    for (int i = 1; i <= batchSize; i++) {
      batchInsert(batchSize, securityUserUtil.getId(), start, end);
    }
  }
  
  private void batchInsert(
      final Integer batchSize,
      final Long userId,
      final LocalDateTime start,
      final LocalDateTime end
  ) {
    final String sql = "INSERT INTO schedules (event, event_date, memo, status, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    final LocalDateTime randomDateTime = generateRandomLocalDateTime(start, end);
    
    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(@NonNull final PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, generateRandomEvent());
        ps.setObject(2, randomDateTime);
        ps.setString(3, generateRandomMemo());
        ps.setString(4, generateRandomStatus().getKey());
        ps.setLong(5, userId);
        ps.setObject(6, LocalDateTime.now());
        ps.setObject(7, LocalDateTime.now());
      }
      
      @Override
      public int getBatchSize() {
        return batchSize;
      }
    });
  }
  
  private LocalDateTime generateRandomLocalDateTime(LocalDateTime start, LocalDateTime end) {
    long startEpochSecond = start.toEpochSecond(ZoneOffset.UTC);
    long endEpochSecond = end.toEpochSecond(ZoneOffset.UTC);
    long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);
    
    return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
  }
  
  private ScheduleStatus generateRandomStatus() {
    final ScheduleStatus[] statuses = ScheduleStatus.values();
    return statuses[new Random().nextInt(statuses.length)];
  }
  
  private String generateRandomMemo() {
    return "랜덤 메모 " + new Random().nextInt(10000);
  }
  
  private String generateRandomEvent() {
    return "랜덤 행사 " + new Random().nextInt(10000);
  }
}
