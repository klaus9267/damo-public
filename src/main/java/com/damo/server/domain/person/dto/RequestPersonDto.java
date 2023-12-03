package com.damo.server.domain.person.dto;

public record RequestPersonDto(
        String name,
        String relation,
        String memo,
        Long userId
) {}
