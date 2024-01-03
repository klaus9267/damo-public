package com.damo.server.application.controller;

import com.damo.server.application.controller.operation.person.PersonOperationWithBody;
import com.damo.server.application.controller.operation.person.PersonOperationWithNoBody;
import com.damo.server.application.controller.operation.person.PersonOperationWithPagination;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeoplePaginationResponseDto;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.service.PersonReadService;
import com.damo.server.domain.person.service.PersonWriteService;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
            @AuthenticationPrincipal final UserDto user
    ) {
        final PeoplePaginationResponseDto peoplePagination = personReadService.readPeopleByUserIdAndRelation(paginationParam, user.getId());

        return ResponseEntity.ok(peoplePagination);
    }

    @PersonOperationWithBody(summary = "대상 추가", description = "대상을 추가함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void addPerson(
            @RequestBody @Valid final RequestCreatePersonDto personDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        personWriteService.save(personDto, user.getId());
    }


    @PersonOperationWithBody(summary = "대상 수정", description = "대상을 수정함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{personId}")
    public void patchPersonById(
            @PathVariable("personId") @Valid final Long personId,
            @RequestBody final RequestUpdatePersonDto personDto,
            @AuthenticationPrincipal final UserDto user
    ) {
        personWriteService.patchPersonById(personDto, personId, user.getId());
    }


    @PersonOperationWithNoBody(summary = "대상 제거", description = "대상 제거함")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{personId}")
    public void removePersonById(
            @PathVariable("personId") @Valid final Long personId,
            @AuthenticationPrincipal final UserDto user
    ) {
        personWriteService.removePersonById(personId, user.getId());
    }
}
