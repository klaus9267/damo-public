package com.damo.server.domain.person.service;

import com.damo.server.application.config.user_details.SecurityUserUtil;
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
    private final SecurityUserUtil securityUserUtil;

    public PeoplePaginationResponseDto readPeopleByUserIdAndRelation(final PersonPaginationParam paginationParam) {
        final Page<PeopleWithTransactionCountDto> peoplePage = personRepository.findAllPeopleWithTransactionCount(paginationParam.toPageable(), securityUserUtil.getId(), paginationParam.getKeyword());

        return PeoplePaginationResponseDto.from(peoplePage);
    }
}
