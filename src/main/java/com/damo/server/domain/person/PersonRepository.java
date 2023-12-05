package com.damo.server.domain.person;

import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByNameAndRelationAndUserId(String name, String relation, Long userId);

    @Query("SELECT new com.damo.server.domain.person.dto.PeopleWithScheduleCountDto(p, COUNT(s)) FROM Person p LEFT JOIN p.schedules s WHERE (:relation IS NULL OR p.relation = :relation) GROUP BY p.id")
    Page<PeopleWithScheduleCountDto> findAllPeopleWithScheduleCount(Pageable pageable, String relation);

    Optional<Person> findByIdAndUserId(final Long personId, final Long userId);
}
