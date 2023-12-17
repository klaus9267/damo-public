package com.damo.server.application.controller;

import com.damo.server.application.config.oauth.PrincipalDetails;
import com.damo.server.domain.common.pagination.param.PersonPaginationParam;
import com.damo.server.domain.person.dto.PeopleWithScheduleCountDto;
import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.dto.RequestPersonDto;
import com.damo.server.domain.person.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PERSON")
@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    @Operation(summary = "대상 목록 조회 페이지네이션", description = "대상 목록 페이지네이션")
    @ApiResponse(responseCode = "200", description = "페이지네이션 처리된 데이터 응답", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping
    public ResponseEntity<?> readPeopleByRelation(
            // TODO: PersonPaginationParam docs 적용
            @Valid
            final PersonPaginationParam paginationParam,
            @Parameter(name = "relation", description = "관계 기준", example = "가족")
            @RequestParam(required = false)
            @Length(max = 10)
            final String relation
    ) {
        final Page<PeopleWithScheduleCountDto> people = personService.readPeopleByRelation(paginationParam.toPageable(), relation);
        return ResponseEntity.ok(people);
    }

    @Operation(summary = "대상 추가", description = "대상을 추가함")
    @ApiResponse(responseCode = "201", description = "대상을 추가함", content = @Content(schema = @Schema(implementation = PersonDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void addPerson(
            @RequestBody
            @Valid
            final RequestPersonDto personDto,
            @AuthenticationPrincipal
            final PrincipalDetails principalDetails
    ) {
        personService.save(personDto, principalDetails.getUser().getId());
    }


    @Operation(summary = "대상 수정", description = "대상을 수정함")
    @ApiResponse(responseCode = "200", description = "성공적으로 수정함", content = @Content(schema = @Schema(implementation = PersonDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{personId}")
    public void patchPersonById(
            @PathVariable("personId")
            final Long personId,
            @RequestBody
            final RequestPersonDto personDto,
            @AuthenticationPrincipal
            final PrincipalDetails principalDetails
    ) {
        personService.patchPersonById(personDto, personId, principalDetails.getUser().getId());
    }


    @Operation(summary = "대상 제거", description = "대상 제거함")
    @ApiResponse(responseCode = "204", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{personId}")
    public void removePersonById(
            @PathVariable("personId")
            final Long personId,
            @AuthenticationPrincipal
            final PrincipalDetails principalDetails) {
        personService.removePersonById(personId, principalDetails.getUser().getId());
    }
}
