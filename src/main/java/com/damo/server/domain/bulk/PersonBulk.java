package com.damo.server.domain.bulk;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.domain.person.entity.PersonRelation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@code PersonBulk}는 대량의 데이터 처리를 위한 Repository 클래스입니다.
 */
@Repository
@RequiredArgsConstructor
public class PersonBulk {
  private final JdbcTemplate jdbcTemplate;
  private final SecurityUserUtil securityUserUtil;

  /**
   * 데이터베이스에서 persons 테이블의 모든 데이터를 삭제합니다.
   */
  @Transactional
  public void clear() {
    final String sql = "DELETE FROM persons";
    jdbcTemplate.execute(sql);
  }

  /**
   * 지정된 크기만큼의 랜덤 데이터를 생성하여 persons 테이블에 삽입합니다.
   *
   * @param batchSize 한 번에 삽입할 데이터의 크기
   * @param id  사용자 ID
   */
  @Transactional
  public void bulkInsert(final Integer batchSize) {
    final Long userId = securityUserUtil.getId();
    for(int i = 1; i <= batchSize; i++) {
      batchInsert(batchSize, userId);
    }
  }

  private void batchInsert(final Integer batchSize, final Long userId) {
    final String sql = "INSERT INTO persons (name, relation, memo, contact, user_id, created_at, updated_at) values ( ?, ?, ?, ?, ?, ?, ? )";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, generateRandomName());
        ps.setString(2, generateRandomRelation().getKey());
        ps.setString(3, generateRandomMemo());
        ps.setString(4, generateRandomContact());
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

  private String generateRandomName() {
    Random random = new Random();

    int length = random.nextInt(8) + 3; // 3~10자 사이의 길이를 랜덤으로 생성
    StringBuilder hangulString = new StringBuilder();

    for (int i = 0; i < length; i++) {
      char randomHangulChar = (char) (0xAC00 + random.nextInt(11172)); // 한글 음절 범위 내의 랜덤 유니코드
      hangulString.append(randomHangulChar);
    }

    return hangulString.toString();
  }

  private PersonRelation generateRandomRelation() {
    final PersonRelation[] relations = PersonRelation.values();
    return relations[new Random().nextInt(relations.length)];
  }

  private String generateRandomMemo() {
    return "랜덤 메모 " + new Random().nextInt(10000);
  }

  private String generateRandomContact() {
    return "010" + String.format("%08d", new Random().nextInt(100000000));
  }
}

