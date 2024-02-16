package com.damo.server.application.controller;

import com.damo.server.domain.bulk.PersonBulk;
import com.damo.server.domain.bulk.ScheduleBulk;
import com.damo.server.domain.bulk.TransactionBulk;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code BulkController}는 대량 데이터 처리를 위한 API를 제공하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkController {
  private final PersonBulk personBulk;
  private final TransactionBulk transactionBulk;
  private final ScheduleBulk scheduleBulk;

  /**
   * 대량의 대상 데이터를 추가하는 API입니다.
   *
   * @param size 대량 데이터 크기
   * @param user 현재 사용자 정보
   */
  @PostMapping("/persons")
  @ResponseStatus(HttpStatus.CREATED)
  public void bulkPerson(
      @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
      @Valid
      @Max(1000)
      @RequestParam("size") final Integer size
  ) {
    personBulk.bulkInsert(size);
  }


  /**
   * 대량의 거래 데이터를 추가하는 API입니다.
   *
   * @param size 대량 데이터 크기
   * @param personId 대상 식별자
   * @param start 랜덤 날짜 시작일
   * @param end 랜덤 날짜 종료일
   */
  @PostMapping("/transactions")
  @ResponseStatus(HttpStatus.CREATED)
  public void bulkTransaction(
      @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
      @Valid
      @Max(1000)
      @RequestParam("size") final Integer size,
      @RequestParam("personId") final Long personId,
      @Parameter(example = "2023-10-01T00:00:00", description = "랜덤 날짜 시작일")
      @RequestParam("start") final LocalDateTime start,
      @Parameter(example = "2024-02-01T00:00:00", description = "랜덤 날짜 종료일")
      @RequestParam("end") final LocalDateTime end
  ) {
    transactionBulk.bulkInsertWithSchedule(size, personId, start, end);
  }

  /**
   * 대량의 일정 데이터를 추가하는 API입니다.
   *
   * @param size 대량 데이터 크기
   * @param start 랜덤 날짜 시작일
   * @param end 랜덤 날짜 종료일
   */
  @PostMapping("/schedules")
  @ResponseStatus(HttpStatus.CREATED)
  public void bulkSchedule(
      @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
      @Valid
      @Max(1000)
      @RequestParam("size") final Integer size,
      @Parameter(example = "2023-10-01T00:00:00", description = "랜덤 날짜 시작일")
      @RequestParam("start") final LocalDateTime start,
      @Parameter(example = "2024-02-01T00:00:00", description = "랜덤 날짜 종료일")
      @RequestParam("end") final LocalDateTime end
  ) {
    scheduleBulk.bulkInsertWithSchedule(size, start, end);
  }


  /**
   * 대량의 대상 데이터를 삭제하는 API입니다.
   */
  @DeleteMapping("/persons")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearPerson() {
    personBulk.clear();
  }

  /**
   * 대량의 거래 데이터를 삭제하는 API입니다.
   */
  @DeleteMapping("/transactions")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearTransaction() {
    transactionBulk.clear();
  }

  /**
   * 대량의 일정 데이터를 삭제하는 API입니다.
   */
  @DeleteMapping("/schedules")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearSchedule() {
    scheduleBulk.clear();
  }
}
