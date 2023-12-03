package com.damo.server.domain.person;

import com.damo.server.application.handler.exception.BadRequestException;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.PersonWithScheduleCount;
import com.damo.server.domain.person.dto.RequestPersonDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public PersonDto save(final RequestPersonDto personDto) {
        // TODO: 동명이인일 경우 어떻게 해결할 것인가?
        if(personRepository.existsByNameAndRelationAndUserId(personDto.name(), personDto.relation(), personDto.userId())) {
            throw new BadRequestException("관계 내에서 동일한 이름이 존재");
        };

        return PersonDto.toPersonDto(this.personRepository.save(Person.toPersonFromRequest(personDto)));
    }

    public Page<PersonWithScheduleCount> readPeopleByRelation(Pageable pageable, String relation) {
        return personRepository.findAllPeopleWithScheduleCount(pageable, relation);
    }
}
