package com.damo.server.domain.person.dto;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.schedule.Schedule;
import com.damo.server.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class PersonDto {
    private final Long id;
    private final String name;
    private final String relation;
    private final String memo;
    private final List<Schedule> schedules;
    private final User user;
    private final Timestamp createdAt;

    public static PersonDto toPersonDto(final Person person) {
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getRelation(),
                person.getMemo(),
                person.getSchedules(),
                person.getUser(),
                person.getCreatedAt()
        );
    }
}
