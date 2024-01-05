package com.damo.server.domain.person.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PeoplePaginationResponseDto {
    private final Integer totalPages;
    private final Long totalElements;
    private final List<PeopleWithTransactionCountDto> people;

    private PeoplePaginationResponseDto(final Page<PeopleWithTransactionCountDto> peoplePage) {
        this.totalPages = peoplePage.getTotalPages();
        this.totalElements = peoplePage.getTotalElements();
        this.people = peoplePage.getContent();
    }

    public static PeoplePaginationResponseDto from(final Page<PeopleWithTransactionCountDto> peoplePage) {
        return new PeoplePaginationResponseDto(peoplePage);
    }
}
