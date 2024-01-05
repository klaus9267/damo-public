package com.damo.server.domain.person.service;

import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.person.dto.PeopleWithTransactionCountDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonReadService {
    private final PersonRepository personRepository;

    public PeoplePaginationResponseDto readPeopleByUserIdAndRelation(final PersonPaginationParam paginationParam, final Long userId) {
        final Page<PeopleWithTransactionCountDto> peoplePage = personRepository.findAllPeopleWithTransactionCount(paginationParam.toPageable(), userId, paginationParam.getKeyword());

        return PeoplePaginationResponseDto.from(peoplePage);
    }
}
