package com.damo.server.domain.person.repository;

import com.damo.server.domain.person.dto.PeopleWithTransactionCountDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByNameAndRelationAndUserId(final String name, final PersonRelation relation, final Long userId);

    @Query(
        """
        SELECT new com.damo.server.domain.person.dto.PeopleWithTransactionCountDto(p, COUNT(t))
        FROM Person p
        LEFT JOIN p.transactions t
        WHERE p.user.id = :userId AND (:keyword IS NULL OR CONCAT(p.name, p.contact) LIKE :keyword)
        GROUP BY p.id
        """
    )
    Page<PeopleWithTransactionCountDto> findAllPeopleWithTransactionCount(
            final Pageable pageable,
            @Param("userId") final Long userId,
            @Param("keyword") final String keyword
    );

    Optional<Person> findByIdAndUserId(final Long personId, final Long userId);

    List<Person> findAllByUserId(final Long userId);

    void deleteByIdAndUserId(final Long personId, final Long userId);
}
