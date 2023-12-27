package com.damo.server.domain.person.service;

import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonReadService {
    private final PersonRepository personRepository;

    public Page<PeopleWithScheduleCountDto> readPeopleByUserIdAndRelation(final PersonPaginationParam paginationParam, final Long userId) {
        return personRepository.findAllPeopleWithScheduleCount(paginationParam.toPageable(), userId, paginationParam.getRelation());
    }
}
