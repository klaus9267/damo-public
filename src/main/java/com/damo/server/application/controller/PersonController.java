package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.PrincipalDetails;
import com.damo.server.application.controller.operation.person.PersonOperationWithBody;
import com.damo.server.application.controller.operation.person.PersonOperationWithNoBody;
import com.damo.server.application.controller.operation.person.PersonOperationWithPagination;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.service.PersonReadService;
import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.person.service.PersonWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PERSON")
@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonWriteService personWriteService;
    private final PersonReadService personReadService;

    @PersonOperationWithPagination(summary = "대상 목록 조회 페이지네이션", description = "대상 목록 페이지네이션")
    @GetMapping
    public ResponseEntity<?> readPeopleByUserIdAndRelation(
            @ParameterObject @Valid final PersonPaginationParam paginationParam,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        final Page<PeopleWithScheduleCountDto> people = personReadService.readPeopleByUserIdAndRelation(paginationParam, principalDetails.getUser().getId());
        return ResponseEntity.ok(people);
    }

    @PersonOperationWithBody(summary = "대상 추가", description = "대상을 추가함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void addPerson(
            @RequestBody @Valid final RequestPersonDto personDto,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        personWriteService.save(personDto, principalDetails.getUser().getId());
    }


    @PersonOperationWithBody(summary = "대상 수정", description = "대상을 수정함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{personId}")
    public void patchPersonById(
            @PathVariable("personId") @Valid final Long personId,
            @RequestBody final RequestPersonDto personDto,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        personWriteService.patchPersonById(personDto, personId, principalDetails.getUser().getId());
    }


    @PersonOperationWithNoBody(summary = "대상 제거", description = "대상 제거함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{personId}")
    public void removePersonById(
            @PathVariable("personId") @Valid final Long personId,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        personWriteService.removePersonById(personId, principalDetails.getUser().getId());
    }
}
