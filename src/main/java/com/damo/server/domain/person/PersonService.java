package com.damo.server.domain.person;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.application.handler.exception.NotFoundException;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public void save(final RequestPersonDto personDto, final Long userId) {
        // TODO: 동명이인일 경우 어떻게 해결할 것인가?
        if(personRepository.existsByNameAndRelationAndUserId(personDto.name(), personDto.relation(), userId)) {
            throw new BadRequestException("관계 내에서 동일한 이름이 존재");
        };

        personRepository.save(Person.toPersonFromRequest(personDto, userId));
    }

    public Page<PeopleWithScheduleCountDto> readPeopleByUserIdAndRelation(final PersonPaginationParam paginationParam, final Long userId) {
        return personRepository.findAllPeopleWithScheduleCount(paginationParam.toPageable(), userId, paginationParam.getRelation());
    }

    @Transactional
    public void patchPersonById(final RequestPersonDto personDto, final Long personId, final Long userId) {
        final Person person = personRepository.findByIdAndUserId(personId, userId).orElseThrow(() -> new NotFoundException("수정할 대상을 찾을 수 없음"));
        person.changeInfo(personDto);
    }

    @Transactional
    public void removePersonById(final Long personId, final Long userId) {
        personRepository.findByIdAndUserId(personId, userId).orElseThrow(() -> new NotFoundException("삭제할 대상을 찾을 수 없음"));
        personRepository.deleteByIdAndUserId(personId, userId);
    }
}
