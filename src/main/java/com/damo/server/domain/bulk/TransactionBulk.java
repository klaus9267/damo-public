package com.damo.server.domain.bulk;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.domain.schedule.ScheduleStatus;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@code TransactionBulk} 클래스는 데이터베이스와 관련된 거래 일괄 작업을 수행합니다.
 * 거래를 삭제하거나 무작위 데이터를 사용하여 거래 및 관련 일정을 일괄 삽입하는 메서드를 제공합니다.
 */
@Repository
@RequiredArgsConstructor
public class TransactionBulk {
  private final JdbcTemplate jdbcTemplate;
  private final SecurityUserUtil securityUserUtil;

  /**
   * 데이터베이스에서 모든 거래를 삭제합니다.
   */
  @Transactional
  public void clear() {
    final String sql = "DELETE FROM transactions";
    jdbcTemplate.execute(sql);
  }

  /**
   * 무작위 데이터를 사용하여 거래 및 일정을 일괄 삽입하고 현재 사용자와 연결합니다.
   *
   * @param batchSize 무작위로 삽입할 거래의 배치 크기입니다.
   * @param personId 거래에 연결된 사람의 식별자입니다.
   * @param start 무작위 거래 및 일정 날짜를 생성하는 데 사용되는 시작 날짜 범위입니다.
   * @param end 무작위 거래 및 일정 날짜를 생성하는 데 사용되는 종료 날짜 범위입니다.
   */
  @Transactional
  public void bulkInsertWithSchedule(
      final Integer batchSize,
      final Long personId,
      final LocalDateTime start,
      final LocalDateTime end
  ) {
    for (int i = 1; i <= batchSize; i++) {
      batchInsert(batchSize, securityUserUtil.getId(), personId, start, end);
    }
  }

  private void batchInsert(
      final Integer batchSize,
      final Long userId,
      final Long personId,
      final LocalDateTime start,
      final LocalDateTime end
  ) {
    final String transactionSql = "INSERT INTO transactions (amount, action, category, memo, user_id, person_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.batchUpdate(
        connection -> connection.prepareStatement(transactionSql, Statement.RETURN_GENERATED_KEYS),
        new BatchPreparedStatementSetter() {
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setLong(1, generateRandomAmount());
            ps.setString(2, generateRandomAction().getKey());
            ps.setString(3, generateRandomCategory().getKey());
            ps.setString(4, generateRandomMemo());
            ps.setLong(5, userId);
            ps.setLong(6, personId);
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
          }

          public int getBatchSize() {
            return batchSize;
          }
        },
        keyHolder
    );
    final List<Long> generatedTransactionIds = keyHolder
        .getKeyList()
        .stream()
        .map(keyMap -> ((BigInteger) keyMap.get("GENERATED_KEY")).longValue())
        .toList();
    insertSchedulesForTransactions(generatedTransactionIds, userId, jdbcTemplate, start, end);
  }

  private void insertSchedulesForTransactions(
      final List<Long> transactionIds,
      final Long userId,
      final JdbcTemplate jdbcTemplate,
      final LocalDateTime start,
      final LocalDateTime end
  ) {
    final String scheduleSql = "INSERT INTO schedules (event, event_date, memo, status, user_id, transaction_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    final LocalDateTime randomDateTime = generateRandomLocalDateTime(start, end);
    for (Long transactionId : transactionIds) {
      jdbcTemplate.update(scheduleSql,
          ps -> {
            ps.setString(1, generateRandomEvent());
            ps.setObject(2, randomDateTime);
            ps.setString(3, generateRandomMemo());
            ps.setString(4, generateRandomStatus().getKey());
            ps.setLong(5, userId);
            ps.setLong(6, transactionId);
            ps.setTimestamp(7, Timestamp.valueOf(randomDateTime));
            ps.setTimestamp(8, Timestamp.valueOf(randomDateTime));
          }
      );
    }
  }

  private Long generateRandomAmount() {
    final Random random = new Random();

    long minAmount = 10000L;
    long maxAmount = 1000000L;

    return random.nextLong(maxAmount - minAmount + 1) + minAmount;
  }

  private LocalDateTime generateRandomLocalDateTime(LocalDateTime start, LocalDateTime end) {
    long startEpochSecond = start.toEpochSecond(ZoneOffset.UTC);
    long endEpochSecond = end.toEpochSecond(ZoneOffset.UTC);
    long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

    return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
  }

  private TransactionAction generateRandomAction() {
    final TransactionAction[] actions = {TransactionAction.RECEIVING, TransactionAction.GIVING};
    return actions[new Random().nextInt(actions.length)];
  }

  private TransactionCategory generateRandomCategory() {
    final TransactionCategory[] categories = TransactionCategory.values();
    return categories[new Random().nextInt(categories.length)];
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
