package com.damo.server.domain.person.repository;

import com.damo.server.domain.person.dto.PeopleWithTransactionCountDto;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * `PersonRepository`는 `Person` 엔터티에 대한 데이터베이스 액세스를 제공하는 JpaRepository입니다.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
  /**
   * 주어진 이름, 관계, 사용자 ID에 해당하는 개인이 존재하는지 여부를 확인합니다.
   *
   * @param name    확인할 개인의 이름
   * @param relation  확인할 개인의 관계
   * @param userId  확인할 개인의 사용자 ID
   * @return      개인이 존재하는 경우 true, 그렇지 않은 경우 false
   */
  Boolean existsByNameAndRelationAndUserId(
      final String name,
      final PersonRelation relation,
      final Long userId
  );

  /**
   * 사용자 ID에 해당하는 모든 개인과 해당 개인의 거래 횟수를 포함하는 페이지를 반환합니다.
   *
   * @param pageable  페이지 정보
   * @param userId  조회할 사용자의 ID
   * @param keyword   검색 키워드 (이름 또는 연락처와 일치해야 함)
   * @return      페이지에 포함된 개인 및 거래 횟수 정보
   */
  @Query(
      """
      SELECT new com.damo.server.domain.person.dto.PeopleWithTransactionCountDto(p, COUNT(t))
      FROM Person p
      LEFT JOIN p.transactions t
      WHERE p.user.id = :userId AND (:keyword IS NULL OR CONCAT(p.name, p.contact) LIKE :keyword)
      GROUP BY p.id
      """
  )
  Page<PeopleWithTransactionCountDto> findAllPeopleWithTransactionCount(
      final Pageable pageable,
      @Param("userId") final Long userId,
      @Param("keyword") final String keyword
  );

  /**
   * 주어진 개인 ID와 사용자 ID에 해당하는 개인을 반환합니다.
   *
   * @param personId  조회할 개인의 ID
   * @param userId  조회할 사용자의 ID
   * @return      주어진 ID와 사용자 ID에 해당하는 개인 (존재하지 않을 경우 빈 Optional)
   */
  Optional<Person> findByIdAndUserId(final Long personId, final Long userId);

  /**
   * 사용자 ID에 해당하는 모든 개인을 반환합니다.
   *
   * @param userId  조회할 사용자의 ID
   * @return      사용자 ID에 해당하는 모든 개인 목록
   */
  List<Person> findAllByUserId(final Long userId);

  /**
   * 주어진 개인 ID와 사용자 ID에 해당하는 개인을 삭제합니다.
   *
   * @param personId  삭제할 개인의 ID
   * @param userId  삭제할 개인의 사용자 ID
   */
  void deleteByIdAndUserId(final Long personId, final Long userId);
}
