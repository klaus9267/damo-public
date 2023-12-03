package com.damo.server.application.controller;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.person.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PersonDto addPerson(@RequestBody final RequestPersonDto personDto) {
        return this.personService.save(personDto);
    }
}
