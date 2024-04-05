package com.damo.server.domain.transaction;

import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.schedule.entity.ScheduleStatus;
import com.damo.server.domain.transaction.entity.Transaction;
import com.damo.server.domain.transaction.entity.TransactionAction;
import com.damo.server.domain.transaction.entity.TransactionAmount;
import com.damo.server.domain.transaction.entity.TransactionCategory;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private UserRepository userRepository;

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;
    Transaction transaction;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

      User user = new User(1L, "name", "email", UserRole.USER, "username", "imageUrl", OAuthProviderType.KAKAO, "provideId");
      User savedUser = userRepository.save(user);

      Person person = new Person(1L, "name", PersonRelation.ETC, "01012341234", "memo", null, null, savedUser);
      Person savedPerson = personRepository.save(person);

      Schedule schedule = new Schedule(1L, "event", now, "memo", ScheduleStatus.NORMAL, null, null, savedUser, null);
      TransactionAmount amount = new TransactionAmount(TransactionAction.GIVING, 1000L);

      transaction = new Transaction(1L, amount, "memo", null, null, TransactionCategory.ETC, savedPerson, savedUser, schedule);
      transaction = transactionRepository.save(transaction);
    }

    @Test
    void 내역_생성() {
      assertThat(transaction).extracting(
              Transaction::getId,
              Transaction::getMemo,
              Transaction::getCategory,
              Transaction::getUser,
              Transaction::getPerson
          )
          .containsExactly(
              transaction.getId(),
              transaction.getMemo(),
              transaction.getCategory(),
              transaction.getUser(),
              transaction.getPerson()
          );
    }

    @Test
    void 내역_조회() {
      Transaction foundTransaction = transactionRepository.findById(transaction.getId()).orElseThrow(ExceptionThrowHelper.throwNotFound("조회할 거래 내역을 찾을 수 없음"));

      assertThat(foundTransaction.getId()).isEqualTo(transaction.getId());
      assertThat(foundTransaction.getTransactionAmount()).isEqualTo(transaction.getTransactionAmount());
      assertThat(foundTransaction.getMemo()).isEqualTo(transaction.getMemo());
      assertThat(foundTransaction.getCategory()).isEqualTo(transaction.getCategory());
      assertThat(foundTransaction.getUser()).isEqualTo(transaction.getUser());
      assertThat(foundTransaction.getPerson()).isEqualTo(transaction.getPerson());
    }

    @Test
    void 내역_삭제() {
      Optional<Transaction> foundTransaction1 = transactionRepository.findById(transaction.getId());
      assertThat(foundTransaction1.isPresent()).isEqualTo(true);

      transactionRepository.deleteById(transaction.getId());

      Optional<Transaction> foundTransaction2 = transactionRepository.findById(transaction.getId());
      assertThat(foundTransaction2.isPresent()).isEqualTo(false);
    }
  }
}