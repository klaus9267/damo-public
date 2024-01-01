package com.damo.server.application.controller;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.user.dto.UserDto;
import com.damo.server.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "USER")
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserReadService userReadService;

    @ApiResponse(responseCode = "200", description = "유저가 등록한 대상 목록 조회", useReturnTypeSchema = true)
    @GetMapping("me/persons")
    public ResponseEntity<List<PersonDto>> readPersonsByUserId(@AuthenticationPrincipal final UserDto user) {
        final List<PersonDto> people = userReadService.readPersonsByUserId(user.getId());
        return ResponseEntity.ok(people);
    }
}
