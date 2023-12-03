package com.damo.server.application.controller;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.PersonWithScheduleCount;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.person.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PersonDto addPerson(@RequestBody final RequestPersonDto personDto) {
        // TODO: userId는 security에서 제공하는 데이터로 변경
        return personService.save(personDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Page<PersonWithScheduleCount> readPeopleByTransaction(Pageable pageable, @RequestParam(required = false) String relation) {
        // TODO: Validation 적용해야 함
        return personService.readPeopleByTransaction(pageable, relation);
    }
}
