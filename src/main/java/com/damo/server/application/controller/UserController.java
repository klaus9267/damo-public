package com.damo.server.application.controller;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.user.dto.UserDto;
import com.damo.server.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code UserController}는 사용자 관련 API를 제공하는 컨트롤러 클래스입니다.
 */
@Tag(name = "USER")
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserReadService userReadService;

  /**
   * 현재 사용자가 등록한 대상 목록을 조회하는 API입니다.
   *
   * @return 대상 목록 응답
   */
  @ApiResponse(responseCode = "200", description = "유저가 등록한 대상 목록 조회", useReturnTypeSchema = true)
  @GetMapping("me/persons")
  public ResponseEntity<List<PersonDto>> readPersonsByUserId() {
    final List<PersonDto> people = userReadService.readPersonsByUserId();
    return ResponseEntity.ok(people);
  }
}
