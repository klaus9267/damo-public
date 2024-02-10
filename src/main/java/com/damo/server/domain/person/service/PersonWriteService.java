package com.damo.server.domain.person.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * `PersonWriteService`는 개인 정보를 추가, 수정, 삭제하는 서비스를 제공합니다.
 */
@AllArgsConstructor
@Service
public class PersonWriteService {
  private final PersonRepository personRepository;
  private final SecurityUserUtil securityUserUtil;

  /**
   * 새로운 개인 정보를 추가합니다.
   *
   * @param personDto 추가할 개인 정보를 담은 DTO
   */
  @Transactional
  public void addPerson(final RequestCreatePersonDto personDto) {
    final boolean isExistedPerson = personRepository.existsByNameAndRelationAndUserId(
        personDto.name(),
        personDto.relation(),
        securityUserUtil.getId()
    );
    if (Boolean.TRUE.equals(isExistedPerson)) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "관계 내에서 동일한 이름이 존재");
    }

    personRepository.save(Person.toPersonFromRequest(personDto, securityUserUtil.getId()));
  }

  /**
   * 지정된 개인 ID에 해당하는 개인 정보를 수정합니다.
   *
   * @param personDto 수정할 개인 정보를 담은 DTO
   * @param personId  수정할 개인의 ID
   */
  @Transactional
  public void patchPersonById(final RequestUpdatePersonDto personDto, final Long personId) {
    final Person person = personRepository
        .findByIdAndUserId(personId, securityUserUtil.getId())
        .orElseThrow(ExceptionThrowHelper.throwNotFound("수정할 대상을 찾을 수 없음"));

    person.changeInfo(personDto);
  }

  /**
   * 지정된 개인 ID에 해당하는 개인 정보를 삭제합니다.
   *
   * @param personId 삭제할 개인의 ID
   */
  @Transactional
  public void removePersonById(final Long personId) {
    final Person person = personRepository
        .findByIdAndUserId(personId, securityUserUtil.getId())
        .orElseThrow(ExceptionThrowHelper.throwNotFound("삭제할 대상을 찾을 수 없음"));

    personRepository.deleteByIdAndUserId(person.getId(), securityUserUtil.getId());
  }
}
