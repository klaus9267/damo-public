package com.damo.server.domain.user;

import com.damo.server.domain.person.Person;
import com.damo.server.domain.person.PersonRepository;
import com.damo.server.domain.person.dto.PersonDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public List<PersonDto> readPersonsByUserId(final Long userId) {
        final List<Person> people = personRepository.findAllByUserId(userId);
        return people.stream().map(PersonDto::toPersonDto).toList();
    }
}
