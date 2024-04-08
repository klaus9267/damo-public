package com.damo.server.domain.transaction;

import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.common.TestUtils;
import com.damo.server.domain.common.exception.ExceptionThrowHelper;
import com.damo.server.domain.common.pagination.param.TransactionPaginationParam;
import com.damo.server.domain.person.entity.Person;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.person.repository.PersonRepository;
import com.damo.server.domain.schedule.ScheduleRepository;
import com.damo.server.domain.schedule.entity.Schedule;
import com.damo.server.domain.transaction.dto.RequestCreateTransactionDto;
import com.damo.server.domain.transaction.dto.TransactionAmountDto;
import com.damo.server.domain.transaction.dto.TransactionTotalAmountDto;
import com.damo.server.domain.transaction.dto.TransactionWithScheduleDto;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    LocalDateTime now;
    Transaction transaction;
    User user;
    Person person;
    Schedule schedule;

    @BeforeEach
    void 초기값() {
      now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

      final User newUser = new User(null, "name", "email", UserRole.USER, "username", "imageUrl", OAuthProviderType.KAKAO, "provideId");
      user = userRepository.save(newUser);

      final Person newPerson = new Person(null, "name", PersonRelation.ETC, "01012341234", "memo", null, null, user);
      person = personRepository.save(newPerson);

      final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
      final RequestCreateTransactionDto transactionDto = new RequestCreateTransactionDto(person.getId(), now, "event", amountDto, TransactionCategory.ETC, "memo");

      transaction = transactionRepository.save(Transaction.from(transactionDto, user.getId()));
      schedule = transaction.getSchedule();
    }

    @Test
    void 내역_생성_선물_종류() {
      final TransactionAmount amount = new TransactionAmount(TransactionAction.GIVING, 1000L);
      final TransactionCategory[] categories = TransactionCategory.values();

      for (final TransactionCategory category : categories) {
        final Transaction newTransaction = new Transaction(null, amount, category.getKey(), null, null, category, person, user, null);
        final Transaction savedTransaction = transactionRepository.save(newTransaction);

        assertCreateTransaction(newTransaction, savedTransaction);
      }
    }

    @Test
    void 내역_생성_거래_종류() {
      final TransactionAction[] actions = TransactionAction.values();

      for (final TransactionAction action : actions) {
        if (TransactionAction.TOTAL.equals(action)) {
          continue;
        }
        final TransactionAmount amount = new TransactionAmount(action, 1000L);
        final Transaction newTransaction = new Transaction(null, amount, action.getKey(), null, null, TransactionCategory.ETC, person, user, null);
        final Transaction savedTransaction = transactionRepository.save(newTransaction);

        assertCreateTransaction(newTransaction, savedTransaction);
      }
    }

    @Test
    void 내역_조회_단건() {
      final Transaction foundTransaction = transactionRepository.findById(transaction.getId()).orElseThrow(ExceptionThrowHelper.throwNotFound("조회할 거래 내역을 찾을 수 없음"));
      assertReadTransaction(foundTransaction);
    }

    @Test
    void 내역_조회_existsByEventDateAndPersonIdAndEvent() {
      final Boolean isTransactionExists = transactionRepository.existsByEventDateAndPersonIdAndEvent(schedule.getEventDate(), person.getId(), schedule.getEvent());
      assertThat(isTransactionExists).isTrue();
    }

    @Test
    void 내역_조회_findByIdAndUserId() {
      final Transaction foundTransaction = transactionRepository.findByIdAndUserId(transaction.getId(), user.getId()).orElseThrow(ExceptionThrowHelper.throwNotFound("조회할 거래 내역을 찾을 수 없음"));
      assertReadTransaction(foundTransaction);
    }

    @Test
    void 내역_조회_findAllByUserId() {
      List<Transaction> transactions = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final RequestCreateTransactionDto transactionDto = new RequestCreateTransactionDto(person.getId(), now, "event" + i, amountDto, TransactionCategory.ETC, "memo");
        transactions.add(Transaction.from(transactionDto, user.getId()));
      }
      transactionRepository.saveAll(transactions);

      final TransactionPaginationParam paginationParam = new TransactionPaginationParam(0, 10, TransactionAction.TOTAL, null, null, null, null);
      final Page<TransactionWithScheduleDto> transactionWithScheduleDtoPage = transactionRepository.findAllByUserId(paginationParam.toPageable(), user.getId(), null, null, TransactionAction.TOTAL);
      final List<Transaction> foundTransactions = transactionRepository.findAll();

      assertThat(transactionWithScheduleDtoPage.getTotalPages()).isEqualTo((int) Math.ceil((double) foundTransactions.size() / 10));
      assertThat(transactionWithScheduleDtoPage.getTotalElements()).isEqualTo(foundTransactions.size());
    }

    @Test
    void 내역_조회_findTotalAmount() {
      saveRandomData();

      final List<Transaction> foundTransactions = transactionRepository.findAll();

      final Map<TransactionAction, Long> amountMap = foundTransactions.stream()
          .collect(Collectors.groupingBy(
                  transaction -> transaction.getTransactionAmount().getAction(),
                  Collectors.summingLong(transaction -> transaction.getTransactionAmount().getAmount())
              )
          );

      final TransactionTotalAmountDto totalAmountDto = transactionRepository.findTotalAmount(user.getId());

      assertThat(totalAmountDto.getTotalGivingAmount()).isEqualTo(amountMap.get(TransactionAction.GIVING));
      assertThat(totalAmountDto.getTotalReceivingAmount()).isEqualTo(amountMap.get(TransactionAction.RECEIVING));
    }

    @Test
    void 내역_조회_readRecentAmounts() {
      saveRandomData();

      final List<Transaction> foundTransactions = transactionRepository.findAll();

      final LocalDateTime startedAt = LocalDateTime.now().minusMonths(2);
      final Map<TransactionAction, Long> amountMap = foundTransactions.stream()
          .filter(data -> data.getSchedule().getEventDate().isAfter(startedAt))
          .collect(Collectors.groupingBy(
                  transaction -> transaction.getTransactionAmount().getAction(),
                  Collectors.summingLong(transaction -> transaction.getTransactionAmount().getAmount())
              )
          );

      final TransactionTotalAmountDto totalAmountDto = transactionRepository.readRecentAmounts(user.getId(), startedAt);

      assertThat(totalAmountDto.getTotalGivingAmount()).isEqualTo(amountMap.get(TransactionAction.GIVING));
      assertThat(totalAmountDto.getTotalReceivingAmount()).isEqualTo(amountMap.get(TransactionAction.RECEIVING));
    }

    @Test
    void 내역_삭제() {
      final Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getId());
      assertThat(foundTransaction.isPresent()).isTrue();

      transactionRepository.deleteById(transaction.getId());

      final Optional<Transaction> deletedTransaction = transactionRepository.findById(transaction.getId());
      assertThat(deletedTransaction.isPresent()).isFalse();
    }


    private void assertReadTransaction(final Transaction foundTransaction) {
      assertThat(foundTransaction).extracting(
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

    private void assertCreateTransaction(final Transaction newTransaction, final Transaction savedTransaction) {
      assertThat(newTransaction).extracting(
              Transaction::getId,
              Transaction::getMemo,
              Transaction::getCategory,
              Transaction::getUser,
              Transaction::getPerson
          )
          .containsExactly(
              savedTransaction.getId(),
              savedTransaction.getMemo(),
              savedTransaction.getCategory(),
              savedTransaction.getUser(),
              savedTransaction.getPerson()
          );
    }

    private void saveRandomData() {
      final List<Transaction> transactions = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        final Random random = new Random();
        final long randomAmount = random.nextLong(1000000);
        final int randomIndex = random.nextInt(2) + 1;
        final LocalDateTime randomDateTime = now.minusDays(random.nextInt(100) + 30);

        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.values()[randomIndex], randomAmount);
        final RequestCreateTransactionDto transactionDto = new RequestCreateTransactionDto(person.getId(), randomDateTime, "event" + i, amountDto, TransactionCategory.ETC, "memo");

        transactions.add(Transaction.from(transactionDto, user.getId()));
      }
      transactionRepository.saveAll(transactions);
    }
  }

  @Nested
  @DisplayName("실패 케이스")
  class 실패 {
    LocalDateTime now;
    Transaction transaction;
    User user;
    Person person;
    Schedule schedule;


    @Nested
    @DisplayName("생성 케이스")
    class 생성 {
      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        final User newUser = new User(null, "name", "email", UserRole.USER, "username", "imageUrl", OAuthProviderType.KAKAO, "provideId");
        user = userRepository.save(newUser);

        final Person newPerson = new Person(null, "name", PersonRelation.ETC, "01012341234", "memo", null, null, user);
        person = personRepository.save(newPerson);

        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final RequestCreateTransactionDto transactionDto = new RequestCreateTransactionDto(person.getId(), now, "event", amountDto, TransactionCategory.ETC, "memo");

        transaction = Transaction.from(transactionDto, user.getId());
      }
      @Test
      void 내역_생성_금액_객체_NULL() {
        TestUtils.setField(transaction, "transactionAmount", null);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }

      @Test
      void 내역_생성_거래_종류_NULL() {
        final TransactionAmount amount = new TransactionAmount(null, 1000L);
        TestUtils.setField(transaction, "transactionAmount", amount);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }

      @Test
      void 내역_생성_거래_금액_NULL() {
        final TransactionAmount amount = new TransactionAmount(TransactionAction.RECEIVING, null);
        TestUtils.setField(transaction, "transactionAmount", amount);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }

      @Test
      void 내역_생성_선물_종류_NULL() {
        TestUtils.setField(transaction, "category", null);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }

      @Test
      void 내역_생성_대상_NULL() {
        TestUtils.setField(transaction, "person", null);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }

      @Test
      void 내역_생성_사용자_NULL() {
        TestUtils.setField(transaction, "user", null);
        assertThatThrownBy(() -> transactionRepository.save(transaction)).isInstanceOf(DataIntegrityViolationException.class);
      }
    }

    @Nested
    @DisplayName("수정 케이스")
    class 수정 {
      @BeforeEach
      void 초기값() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        final User newUser = new User(null, "name", "email", UserRole.USER, "username", "imageUrl", OAuthProviderType.KAKAO, "provideId");
        user = userRepository.save(newUser);

        final Person newPerson = new Person(null, "name", PersonRelation.ETC, "01012341234", "memo", null, null, user);
        person = personRepository.save(newPerson);

        final TransactionAmountDto amountDto = new TransactionAmountDto(TransactionAction.GIVING, 1000L);
        final RequestCreateTransactionDto transactionDto = new RequestCreateTransactionDto(person.getId(), now, "event", amountDto, TransactionCategory.ETC, "memo");

        transaction = transactionRepository.save(Transaction.from(transactionDto, user.getId()));
        schedule = transaction.getSchedule();
      }
    }
  }
}