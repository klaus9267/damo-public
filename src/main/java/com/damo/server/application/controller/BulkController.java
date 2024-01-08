package com.damo.server.application.controller;

import com.damo.server.domain.bulk.PersonBulk;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkController {
    private final PersonBulk personBulk;

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkPerson(
            @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
            @Valid
            @Max(1000)
            @RequestParam("size")
            final Integer size,
            @AuthenticationPrincipal
            final UserDto user
    ) {
        personBulk.bulkInsert(size, user.getId());
    }

    @DeleteMapping("/persons")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearPerson() {
        personBulk.clear();
    }

}
