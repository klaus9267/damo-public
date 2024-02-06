package com.damo.server.domain.common.pagination;

import com.damo.server.domain.transaction.TransactionTotalAmount;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * {@code CustomSchedulePage}는 TransactionWithScheduleDto 목록과 함께 사용자 지정 정보를 포함하는 페이지 클래스입니다.
 * Spring Data의 PageImpl을 확장하며, 거래와 관련된 추가 정보를 저장합니다.
 */
@Getter
public class CustomSchedulePage extends PageImpl<TransactionWithScheduleDto> {
  private final TransactionTotalAmount amounts;

  /**
   * CustomSchedulePage의 생성자로, 주어진 페이지의 내용과 페이징 정보를 사용하여 객체를 초기화합니다.
   * 추가적인 거래 관련 정보는 null로 설정됩니다.
   *
   * @param page Spring Data의 Page 객체
   */
  public CustomSchedulePage(final Page<TransactionWithScheduleDto> page) {
    super(page.getContent(), page.getPageable(), page.getTotalElements());
    this.amounts = null;
  }

  /**
   * CustomSchedulePage의 생성자로, 주어진 페이지의 내용과 페이징 정보, 그리고 거래 총액 정보를 사용하여 객체를 초기화합니다.
   *
   * @param page           Spring Data의 Page 객체
   * @param transactionTotalAmount 거래 총액 정보
   */
  public CustomSchedulePage(
      final Page<TransactionWithScheduleDto> page,
      final TransactionTotalAmount transactionTotalAmount
  ) {
    super(page.getContent(), page.getPageable(), page.getTotalElements());
    this.amounts = transactionTotalAmount;
  }
}