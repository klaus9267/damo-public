package com.damo.server.domain.person;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDto save(final RequestPersonDto personDto) {
        final Person person = this.personRepository.save(Person.toPersonFromRequest(personDto));
        return PersonDto.toPersonDto(person);
    }
}
