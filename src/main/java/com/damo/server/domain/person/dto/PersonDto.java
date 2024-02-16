package com.damo.server.domain.person.dto;

import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code PersonDto}는 대상 정보를 포함한 DTO 클래스입니다.
 */
@Getter
@AllArgsConstructor
public class PersonDto {
  private final Long id;
  private final String name;
  private final String contact;
  private final PersonRelation relation;
  private final String memo;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  /**
   * 주어진 {@code Person} 객체를 {@code PersonDto}로 변환합니다.
   *
   * @param person 변환할 대상 객체
   * @return 변환된 {@code PersonDto}
   */
  public static PersonDto toPersonDto(final Person person) {
    return new PersonDto(
        person.getId(),
        person.getName(),
        person.getContact(),
        person.getRelation(),
        person.getMemo(),
        person.getCreatedAt(),
        person.getUpdatedAt()
    );
  }
}
