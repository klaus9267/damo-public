package com.damo.server.domain.transaction;

import com.damo.server.domain.transaction.dto.TransactionTotalAmountDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * `TransactionRepository`는 `Transaction` 엔터티에 대한 데이터베이스 액세스를 제공하는 JpaRepository입니다.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  /**
   * 주어진 날짜, 대상 ID, 행사에 해당하는 거래내역이 존재하는지 여부를 확인합니다.
   *
   * @param eventDate 확인할 행사 날짜
   * @param personId  확인할 대상 ID
   * @param event     확인할 행사 이름
   * @return 거래 내역이 존재하는 경우 true, 그렇지 않은 경우 false
   */
  @Query("""
         SELECT count(t.id) > 0
         FROM Transaction t
         LEFT JOIN FETCH  Schedule s ON t.id = s.transaction.id
         WHERE t.person.id = :personId AND s.eventDate = :eventDate AND s.event = :event
         """)
  Boolean existsByEventDateAndPersonIdAndEvent(@Param("eventDate") final LocalDateTime eventDate, @Param("personId") final Long personId, @Param("event") final String event);
  
  /**
   * 주어진 내역 ID
   *
   * @param id     조회할 거래 내역의 ID
   * @param userId 조회할 사용자의 ID
   * @return 주어진 ID와 사용자 ID에 해당하는 내역(존재하지 않은 경우 빈 Optional)
   */
  @EntityGraph(attributePaths = "schedule")
  Optional<Transaction> findByIdAndUserId(@Param("id") final Long id, @Param("userId") final Long userId);
  
  /**
   * 주어진 사용자 ID, 거래 종류와 조회 시작부터 종료 날짜이전에 해당하는 모든 거래 내역을 일정과 함께 포함하는 페이지를 반환합니다.
   *
   * @param pageable  페이지 정보
   * @param userId    조회할 사용자의 ID
   * @param startedAt 조회시작 날짜
   * @param endedAt   조회종료 날짜
   * @param action    조회할 거래 종류
   * @return 페이지에 포함된 거래 내역 및 일정
   */
  @Query("""
         SELECT new com.damo.server.domain.transaction.dto.TransactionWithScheduleDto(t, p, s)
         FROM Transaction t
              LEFT JOIN FETCH  Person p ON t.person.id = p.id
              LEFT JOIN FETCH  Schedule s ON t.schedule.id = s.id
         WHERE p.user.id = :userId
              AND (:startedAt IS NULL OR s.eventDate >= :startedAt)
              AND (:endedAt IS NULL OR s.eventDate <= :endedAt)
              AND ('TOTAL' = :action  OR t.transactionAmount.action = :action)
         """)
  Page<TransactionWithScheduleDto> findAllByUserId(
      final Pageable pageable,
      @Param("userId") final Long userId,
      @Param("startedAt") final LocalDateTime startedAt,
      @Param("endedAt") final LocalDateTime endedAt,
      @Param("action") final TransactionAction action
  );
  
  /**
   * 주어진 사용자 ID에 해당하는 거래 내역들을 조회하여 거래 유형에 따라 거래 총액을 반환합니다.
   *
   * @param userId 조회할 사용자 ID
   * @return 전체 거래 총액
   */
  @Query("""
         SELECT new com.damo.server.domain.transaction.TransactionTotalAmount(
               new com.damo.server.domain.transaction.entity.TransactionAmount(
                   SUM(CASE WHEN t.transactionAmount.action = 'GIVING' THEN t.transactionAmount.amount ELSE 0 END), 'GIVING'
               ),
               new com.damo.server.domain.transaction.entity.TransactionAmount(
                   SUM(CASE WHEN t.transactionAmount.action = 'RECEIVING' THEN t.transactionAmount.amount ELSE 0 END), 'RECEIVING'
               )
         )
         FROM Transaction t
              LEFT JOIN FETCH  Person p ON t.person.id = p.id
         WHERE t.user.id = :userId
         """)
  TransactionTotalAmountDto findTotalAmount(@Param("userId") final Long userId);
  
  /**
   * 주어진 사용자 ID와 조회 시작 날짜부터 현재까지의 거래 내역들을 조회하여 거래 유형에 따라 거래 총액을 반환합니다.
   *
   * @param userId    조회할 사용자 ID
   * @param startedAt 조회시작 날짜
   * @return 최근 거래 총액
   */
  @Query("""
         SELECT new com.damo.server.domain.transaction.TransactionTotalAmount(
               new com.damo.server.domain.transaction.entity.TransactionAmount(
                   SUM(CASE WHEN t.transactionAmount.action = 'GIVING' THEN t.transactionAmount.amount ELSE 0 END), 'GIVING'
               ),
               new com.damo.server.domain.transaction.entity.TransactionAmount(
                   SUM(CASE WHEN t.transactionAmount.action = 'RECEIVING' THEN t.transactionAmount.amount ELSE 0 END), 'RECEIVING'
               )
         )
         FROM Transaction t
              LEFT JOIN FETCH  Person p ON t.person.id = p.id
              LEFT JOIN FETCH  Schedule s ON t.id = s.transaction.id
         WHERE t.user.id = :userId AND s.eventDate >= :startedAt
         """)
  TransactionTotalAmountDto readRecentAmounts(@Param("userId") final Long userId, @Param("startedAt") final LocalDateTime startedAt);
}
