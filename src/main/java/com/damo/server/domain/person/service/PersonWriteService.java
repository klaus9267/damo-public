package com.damo.server.domain.person.service;

import com.damo.server.application.handler.exception.CustomErrorCode;
import com.damo.server.application.handler.exception.CustomException;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class PersonWriteService {
    private final PersonRepository personRepository;

    @Transactional
    public void addPerson(final RequestCreatePersonDto personDto, final Long userId) {
        // TODO: 동명이인일 경우 어떻게 해결할 것인가?
        if(personRepository.existsByNameAndRelationAndUserId(personDto.name(), personDto.relation(), userId)) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "관계 내에서 동일한 이름이 존재");
        };

        personRepository.save(Person.toPersonFromRequest(personDto, userId));
    }

    @Transactional
    public void patchPersonById(final RequestUpdatePersonDto personDto, final Long personId, final Long userId) {
        final Person person = personRepository.findByIdAndUserId(personId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "수정할 대상을 찾을 수 없음"));
        person.changeInfo(personDto);
    }

    @Transactional
    public void removePersonById(final Long personId, final Long userId) {
        personRepository.findByIdAndUserId(personId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "삭제할 대상을 찾을 수 없음"));
        personRepository.deleteByIdAndUserId(personId, userId);
    }
}
