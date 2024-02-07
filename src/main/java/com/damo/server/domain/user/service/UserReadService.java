package com.damo.server.domain.user.service;

import com.damo.server.domain.person.dto.PersonDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.user.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 조회하는 서비스 클래스입니다.
 */
@AllArgsConstructor
@Service
public class UserReadService {
  private final UserRepository userRepository;
  private final PersonRepository personRepository;

  /**
   * 주어진 사용자 아이디에 대한 모든 관련 있는 사람들의 정보를 조회합니다.
   *
   * @param userId 조회할 사용자 아이디
   * @return 해당 사용자에 연관된 사람들의 정보를 담은 PersonDto 리스트
   */
  public List<PersonDto> readPersonsByUserId(final Long userId) {
    final List<Person> people = personRepository.findAllByUserId(userId);
    return people.stream().map(PersonDto::toPersonDto).toList();
  }
}
