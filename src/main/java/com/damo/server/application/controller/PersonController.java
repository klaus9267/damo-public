package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.person.PersonCreateOperation;
import com.damo.server.application.controller.operation.person.PersonDeleteOperation;
import com.damo.server.application.controller.operation.person.PersonPaginationOperation;
import com.damo.server.application.controller.operation.person.PersonUpdateOperation;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.service.PersonReadService;
import com.damo.server.domain.person.service.PersonWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code PersonController}는 대상 관련 API를 처리하는 컨트롤러 클래스입니다.
 */
@Tag(name = "PERSON")
@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
  private final PersonWriteService personWriteService;
  private final PersonReadService personReadService;

  /**
   * 대상 목록을 페이지네이션하여 조회하는 API입니다.
   *
   * @param paginationParam 페이지네이션 파라미터 객체
   * @return 대상 목록 페이지네이션 응답 DTO
   */
  @PersonPaginationOperation(summary = "대상 목록 조회 페이지네이션", description = "대상 목록 페이지네이션")
  @GetMapping
  public ResponseEntity<PeoplePaginationResponseDto> readPeopleByUserIdAndRelation(
      @ParameterObject final PersonPaginationParam paginationParam
  ) {
    final PeoplePaginationResponseDto peoplePagination =
        personReadService.readPeopleByUserIdAndRelation(paginationParam);

    return ResponseEntity.ok(peoplePagination);
  }

  /**
   * 대상을 추가하는 API입니다.
   *
   * @param personDto 추가할 대상 정보 DTO
   * @return HTTP 상태코드 201 (Created)
   */
  @PersonCreateOperation(summary = "대상 추가", description = "대상을 추가함")
  @PostMapping
  public ResponseEntity<Void> addPerson(
      @RequestBody @Valid final RequestCreatePersonDto personDto
  ) {
    personWriteService.addPerson(personDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  /**
   * 대상을 수정하는 API입니다.
   *
   * @param personId 대상 식별자
   * @param personDto 수정할 대상 정보 DTO
   * @return HTTP 상태코드 204 (No Content)
   */
  @PersonUpdateOperation(summary = "대상 수정", description = "대상을 수정함")
  @PatchMapping("{personId}")
  public ResponseEntity<Void> patchPersonById(
      @PathVariable("personId") @Valid final Long personId,
      @RequestBody final RequestUpdatePersonDto personDto
  ) {
    personWriteService.patchPersonById(personDto, personId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 대상을 제거하는 API입니다.
   *
   * @param personId 대상 식별자
   * @return HTTP 상태코드 204 (No Content)
   */
  @PersonDeleteOperation(summary = "대상 제거", description = "대상 제거함")
  @DeleteMapping("{personId}")
  public ResponseEntity<Void> removePersonById(
      @PathVariable("personId") @Valid final Long personId
  ) {
    personWriteService.removePersonById(personId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
