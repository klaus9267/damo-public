package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.person.*;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.service.PersonReadService;
import com.damo.server.domain.person.service.PersonWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PERSON")
@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonWriteService personWriteService;
    private final PersonReadService personReadService;

    @PersonPaginationOperation(summary = "대상 목록 조회 페이지네이션", description = "대상 목록 페이지네이션")
    @GetMapping
    public ResponseEntity<?> readPeopleByUserIdAndRelation(@ParameterObject final PersonPaginationParam paginationParam) {
        final PeoplePaginationResponseDto peoplePagination = personReadService.readPeopleByUserIdAndRelation(paginationParam);

        return ResponseEntity.ok(peoplePagination);
    }

    @PersonCreateOperation(summary = "대상 추가", description = "대상을 추가함")
    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody @Valid final RequestCreatePersonDto personDto) {
        personWriteService.addPerson(personDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PersonUpdateOperation(summary = "대상 수정", description = "대상을 수정함")
    @PatchMapping("{personId}")
    public ResponseEntity<?> patchPersonById(
            @PathVariable("personId") @Valid final Long personId,
            @RequestBody final RequestUpdatePersonDto personDto
    ) {
        personWriteService.patchPersonById(personDto, personId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PersonDeleteOperation(summary = "대상 제거", description = "대상 제거함")
    @DeleteMapping("{personId}")
    public ResponseEntity<?> removePersonById(@PathVariable("personId") @Valid final Long personId) {
        personWriteService.removePersonById(personId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
