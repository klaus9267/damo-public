package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.schedule.ScheduleCreateOperation;
import com.damo.server.application.controller.operation.schedule.ScheduleDeleteOperation;
import com.damo.server.application.controller.operation.schedule.ScheduleReadOperation;
import com.damo.server.application.controller.operation.schedule.ScheduleUpdateOperation;
import com.damo.server.domain.common.pagination.param.SchedulePaginationParam;
import com.damo.server.domain.schedule.dto.RequestCreateScheduleDto;
import com.damo.server.domain.schedule.dto.RequestUpdateScheduleDto;
import com.damo.server.domain.schedule.dto.SchedulePaginationResponseDto;
import com.damo.server.domain.schedule.dto.ScheduleWithTransactionDto;
import com.damo.server.domain.schedule.service.ScheduleReadService;
import com.damo.server.domain.schedule.service.ScheduleWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * {@code ScheduleController}는 일정 관련 API를 처리하는 컨트롤러 클래스입니다.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "SCHEDULE")
public class ScheduleController {
  private final ScheduleWriteService scheduleWriteService;
  private final ScheduleReadService scheduleReadServices;
  
  /**
   * 일정을 추가하는 API입니다.
   *
   * @param scheduleDto 추가할 일정 정보 DTO
   */
  @PostMapping
  @ScheduleCreateOperation(summary = "일정 추가")
  public ResponseEntity<Void> addSchedule(@Valid @RequestBody final RequestCreateScheduleDto scheduleDto) {
    scheduleWriteService.addSchedule(scheduleDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
  /**
   * 일정을 단건 조회하는 API입니다.
   *
   * @param scheduleId 대상 식별자
   * @return 일정과 내역 정보 DTO
   */
  @GetMapping("{scheduleId}")
  @ScheduleReadOperation(summary = "일정 단건 조회")
  public ResponseEntity<ScheduleWithTransactionDto> readSchedule(@PathVariable("scheduleId") final Long scheduleId) {
    final ScheduleWithTransactionDto scheduleWithTransactionDto = scheduleReadServices.readSchedule(scheduleId);
    return ResponseEntity.ok(scheduleWithTransactionDto);
  }
  
  /**
   * 기간에 해당하는 일정 목록을 페이지네이션하여 조회하는 API입니다.
   *
   * @param paginationParam 페이지네이션 파라미터 객체
   * @return 일정 목록 페이지네이션 응답 dto
   */
  @GetMapping
  @ScheduleReadOperation(summary = "년,월별 일정 리스트 조회")
  public ResponseEntity<SchedulePaginationResponseDto> readScheduleListByDate(@ParameterObject @Valid final SchedulePaginationParam paginationParam) {
    final SchedulePaginationResponseDto scheduleDto = scheduleReadServices.readScheduleByEventDate(paginationParam);
    return ResponseEntity.ok(scheduleDto);
  }
  
  /**
   * 일정을 수정하는 API입니다.
   *
   * @param scheduleId  대상 식별자
   * @param scheduleDto 수정할 일정 정보 DTO
   * @return 상태코드 204 (No Content)
   */
  @PatchMapping("{scheduleId}")
  @ScheduleUpdateOperation(summary = "일정 수정", description = "내역 생략 가능")
  public ResponseEntity<Void> patchScheduleById(
      @PathVariable("scheduleId") final Long scheduleId,
      @Valid @RequestBody final RequestUpdateScheduleDto scheduleDto
  ) {
    scheduleWriteService.patchScheduleById(scheduleDto, scheduleId);
    return ResponseEntity.noContent().build();
  }
  
  /**
   * 일정을 제거하는 API입니다.
   *
   * @param scheduleId 대상 식별자
   * @return 상태코드 204 (No Content)
   */
  @DeleteMapping("{scheduleId}")
  @ScheduleDeleteOperation(summary = "일정 삭제")
  public ResponseEntity<Void> removeScheduleById(@PathVariable("scheduleId") final Long scheduleId) {
    scheduleWriteService.removeScheduleById(scheduleId);
    return ResponseEntity.noContent().build();
  }
}
