package com.damo.server.domain.user.service;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserReadService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public List<PersonDto> readPersonsByUserId(final Long userId) {
        final List<Person> people = personRepository.findAllByUserId(userId);
        return people.stream().map(PersonDto::toPersonDto).toList();
    }
}
