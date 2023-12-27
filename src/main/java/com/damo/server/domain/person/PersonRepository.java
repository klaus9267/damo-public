package com.damo.server.domain.person;

import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByNameAndRelationAndUserId(final String name, final String relation, final Long userId);

    @Query(
        """
        SELECT new com.damo.server.domain.person.dto.PeopleWithScheduleCountDto(p, COUNT(s)) 
        FROM Person p 
        LEFT JOIN p.schedules s 
        WHERE p.user.id = :userId AND (:relation IS NULL OR p.relation = :relation) 
        GROUP BY p.id
        """
    )
    Page<PeopleWithScheduleCountDto> findAllPeopleWithScheduleCount(final Pageable pageable, @Param("userId") final Long userId, @Param("relation") final String relation);

    Optional<Person> findByIdAndUserId(final Long personId, final Long userId);

    List<Person> findAllByUserId(final Long userId);

    void deleteByIdAndUserId(final Long personId, final Long userId);
}
