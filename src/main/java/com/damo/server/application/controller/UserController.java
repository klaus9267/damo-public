package com.damo.server.application.controller;


import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("{id}/persons")
    public List<PersonDto> readPersonsByUserId(@PathVariable("id") final Long userId) {
        return userService.readPersonsByUserId(userId);
    }
}
