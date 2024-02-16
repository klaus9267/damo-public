package com.damo.server.domain.person.entity;

import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


/**
 * `Person` 클래스는 시스템에서 개인을 나타냅니다.
 * 이 클래스는 데이터베이스의 "persons" 테이블과 매핑됩니다.
 */
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "persons")
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PersonRelation relation;

  @Column
  private String contact;

  @Column(columnDefinition = "TEXT")
  private String memo;

  @Column(name = "created_at")
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Transaction> transactions = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private Person(final RequestCreatePersonDto personDto, final Long userId) {
    this.name = personDto.name();
    this.contact = personDto.contact();
    this.relation = personDto.relation();
    this.memo = personDto.memo();
    this.user = User.builder().id(userId).build();
  }

  /**
   * RequestCreatePersonDto로부터 Person 객체를 생성하는 정적 메서드.
   */
  public static Person toPersonFromRequest(
      final RequestCreatePersonDto personDto,
      final Long userId
  ) {
    return new Person(personDto, userId);
  }

  /**
   * RequestUpdatePersonDto로부터 개인 정보를 업데이트하는 메서드.
   */
  public void changeInfo(final RequestUpdatePersonDto personDto) {
    this.name = personDto.name() != null ? personDto.name() : getName();
    this.contact = personDto.contact() != null ? personDto.contact() : getContact();
    this.relation = personDto.relation() != null ? personDto.relation() : getRelation();
    this.memo = personDto.memo() != null ? personDto.memo() : getMemo();
  }
}
