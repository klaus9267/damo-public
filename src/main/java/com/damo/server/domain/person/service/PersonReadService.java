package com.damo.server.domain.person.service;

import com.damo.server.application.security.user_details.SecurityUserUtil;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
import com.damo.server.domain.person.dto.PeopleWithTransactionCountDto;
import com.damo.server.domain.person.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * `PersonReadService`는 사용자의 관계에 따라 개인을 조회하는 서비스를 제공합니다.
 */
@AllArgsConstructor
@Service
public class PersonReadService {
  private final PersonRepository personRepository;
  private final SecurityUserUtil securityUserUtil;


  /**
   * 사용자 ID와 관계에 따라 개인을 조회하고, 조회 결과를 페이지로 반환합니다.
   *
   * @param paginationParam   조회할 개인 목록의 페이징 및 검색 조건
   * @return          조회된 개인 및 거래 횟수 정보를 포함하는 페이지 응답 DTO
   */
  public PeoplePaginationResponseDto readPeopleByUserIdAndRelation(
      final PersonPaginationParam paginationParam
  ) {
    final Page<PeopleWithTransactionCountDto> peoplePage =
        personRepository.findAllPeopleWithTransactionCount(
            paginationParam.toPageable(),
            securityUserUtil.getId(),
            paginationParam.getKeyword()
        );

    return PeoplePaginationResponseDto.from(peoplePage);
  }
}
