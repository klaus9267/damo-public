package com.damo.server.application.controller;

import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.person.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody final RequestPersonDto personDto) {
        // TODO: userId는 security에서 제공하는 데이터로 변경
        return new ResponseEntity<>(personService.save(personDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> readPeopleByRelation(@Valid final PersonPaginationParam paginationParam, @RequestParam(required = false) @Length(max = 10) final String relation) {
        final Page<PeopleWithScheduleCountDto> people = personService.readPeopleByRelation(paginationParam.toPageable(), relation);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @DeleteMapping("{personId}")
    public ResponseEntity<?> removePersonById(@PathVariable("personId") final Long personId) {
        personService.removePersonById(personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
