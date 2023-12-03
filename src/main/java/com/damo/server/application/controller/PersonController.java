package com.damo.server.application.controller;

import com.damo.server.domain.person.PersonService;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }
}
