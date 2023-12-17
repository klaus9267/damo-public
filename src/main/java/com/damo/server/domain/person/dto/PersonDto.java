package com.damo.server.domain.person.dto;

import com.damo.server.domain.person.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PersonDto {
    private final Long id;
    private final String name;
    private final String contact;
    private final String relation;
    private final String memo;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public static PersonDto toPersonDto(final Person person) {
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getContact(),
                person.getRelation(),
                person.getMemo(),
                person.getCreatedAt(),
                person.getUpdatedAt()
        );
    }
}
