package com.damo.server.application.controller;

import com.damo.server.domain.bulk.PersonBulk;
import com.damo.server.domain.bulk.ScheduleBulk;
import com.damo.server.domain.bulk.TransactionBulk;
import com.damo.server.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkController {
    private final PersonBulk personBulk;
    private final TransactionBulk transactionBulk;
    private final ScheduleBulk scheduleBulk;

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkPerson(
            @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
            @Valid
            @Max(1000)
            @RequestParam("size") final Integer size,
            @AuthenticationPrincipal final UserDto user
    ) {
        personBulk.bulkInsert(size, user.getId());
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkTransaction(
            @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
            @Valid
            @Max(1000)
            @RequestParam("size") final Integer size,
            @RequestParam("personId") final Long personId,
            @Parameter(example = "2023-10-01T00:00:00", description = "랜덤 날짜 시작일")
            @RequestParam("start") final LocalDateTime start,
            @Parameter(example = "2024-02-01T00:00:00", description = "랜덤 날짜 종료일")
            @RequestParam("end") final LocalDateTime end
    ) {
        transactionBulk.bulkInsertWithSchedule(size, personId, start, end);
    }

    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkSchedule(
            @Parameter(example = "1000", description = "size^2 만큼 데이터가 저장됩니다. 1000 입력시 1,000,000개 저장, 1000보다 큰 값 입력시 먹통될 수 있어서 제한합니다.")
            @Valid
            @Max(1000)
            @RequestParam("size") final Integer size,
            @Parameter(example = "2023-10-01T00:00:00", description = "랜덤 날짜 시작일")
            @RequestParam("start") final LocalDateTime start,
            @Parameter(example = "2024-02-01T00:00:00", description = "랜덤 날짜 종료일")
            @RequestParam("end") final LocalDateTime end
    ) {
        scheduleBulk.bulkInsertWithSchedule(size, start, end);
    }

    @DeleteMapping("/persons")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearPerson() {
        personBulk.clear();
    }

    @DeleteMapping("/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearTransaction() {
        transactionBulk.clear();
    }

    @DeleteMapping("/schedules")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearSchedule() {
        scheduleBulk.clear();
    }
}
