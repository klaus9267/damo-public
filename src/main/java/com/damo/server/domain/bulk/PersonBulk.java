package com.damo.server.domain.bulk;


import com.damo.server.domain.person.entity.PersonRelation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class PersonBulk {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void bulkInsert(final Integer batchSize, final Long id) {
        for(int i = 1; i <= batchSize; i++) {
            batchInsert(batchSize, id);
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
        // 구현 필요: 랜덤 이름 생성 로직
        return String.format("%08d", new Random().nextInt(100000)) + new Random().nextInt(10000);
    }

    private PersonRelation generateRandomRelation() {
        final PersonRelation[] relations = PersonRelation.values();
        return relations[new Random().nextInt(relations.length)];
    }

    private String generateRandomMemo() {
        // 구현 필요: 랜덤 메모 생성 로직
        return "랜덤 메모 " + new Random().nextInt(10000);
    }

    private String generateRandomContact() {
        // 구현 필요: 랜덤 전화번호 생성 로직
        return "010" + String.format("%08d", new Random().nextInt(100000000));
    }
}

