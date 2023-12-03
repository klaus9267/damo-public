package com.damo.server.domain.person;

import com.damo.server.domain.person.dto.RequestPersonDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Boolean existsByNameAndRelationAndUserId(String name, String relation, Long userId);
}
