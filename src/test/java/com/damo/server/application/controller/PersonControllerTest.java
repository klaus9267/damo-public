package com.damo.server.application.controller;

import com.damo.server.common.WithMockCustomUser;
import com.damo.server.domain.person.dto.RequestCreatePersonDto;
import com.damo.server.domain.person.dto.RequestUpdatePersonDto;
import com.damo.server.domain.person.entity.PersonRelation;
import com.damo.server.domain.person.service.PersonWriteService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockCustomUser
public class PersonControllerTest {
    @MockBean
    PersonWriteService personWriteService;
    @Autowired
    MockMvc mvc;
    private final String END_POINT = "/api/persons";
    private final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Nested
    @DisplayName("성공 케이스")
    class 성공 {
        LocalDateTime now;

        @BeforeEach
        void 초기값() {
            now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }

        @Test
        void 대상_생성() throws Exception {
            for (final PersonRelation relation : PersonRelation.values()) {
                final RequestCreatePersonDto requestPersonDto = new RequestCreatePersonDto("테스트 이름", relation, "테스트 메모", "01012345678");
                doNothing().when(personWriteService).addPerson(requestPersonDto);

                final String json = mapper.writeValueAsString(requestPersonDto);

                mvc.perform(post(END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
                  ).andDo(print())
                  .andExpect(status().isCreated());
            }
        }

        @Test
        void 대상_수정() throws Exception {
            final long personId = 1L;
            for (final PersonRelation relation : PersonRelation.values()) {
                final RequestUpdatePersonDto requestPersonDto = new RequestUpdatePersonDto("테스트 이름", relation, "테스트 메모", "01012345678");
                doNothing().when(personWriteService).patchPersonById(requestPersonDto, personId);

                final String json = mapper.writeValueAsString(requestPersonDto);

                mvc.perform(patch(END_POINT + "/" + personId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
                  ).andDo(print())
                  .andExpect(status().isNoContent());
            }
        }

        @Test
        void 대상_삭제() throws Exception {
            final long personId = 1L;

            doNothing().when(personWriteService).removePersonById(personId);

            mvc.perform(delete(END_POINT + "/" + personId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
              ).andDo(print())
              .andExpect(status().isNoContent());
        }
    }
    @Nested
    @DisplayName("실패 케이스")
    class 실패 {
        @Nested
        @DisplayName("대상_생성_실패")
        class 대상_생성_실패 {
            private void callApiForBadRequestWhenCreate(final FailPersonDto failPersonDto) throws Exception {
                final String json = mapper.writeValueAsString(failPersonDto);

                mvc.perform(post(END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                  )
                  .andExpect(status().isBadRequest())
                  .andDo(print());
            }
            @Test
            void 대상_이름값_없을_때() throws Exception {
                final FailPersonDto personDto = new FailPersonDto(null, PersonRelation.FAMILY, "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_이름_빈문자열() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("", PersonRelation.FAMILY, "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_이름_20자_초과() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("111111111111111111111", PersonRelation.FAMILY, "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_관계값_없을_때() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", null, "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_관계_빈문자열() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", "", "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_관계_허용되지않은() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", "FASTER", "메모", "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_메모_200자_초과() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", PersonRelation.FAMILY, "글".repeat(201), "01012345678");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_연락처_빈문자열() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", PersonRelation.FAMILY, "메모", "");
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_연락처_8자() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", PersonRelation.FAMILY, "메모", "0".repeat(8));
                callApiForBadRequestWhenCreate(personDto);
            }
            @Test
            void 대상_연락처_12자() throws Exception {
                final FailPersonDto personDto = new FailPersonDto("홍길동", PersonRelation.FAMILY, "메모", "0".repeat(12));
                callApiForBadRequestWhenCreate(personDto);
            }
        }
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class FailPersonDto {
    private final Object name;
    private final Object relation;
    private final Object memo;
    private final Object contact;
    public FailPersonDto(final Object name, final Object relation, final Object memo, final Object contact) {
        this.name = name;
        this.relation = relation;
        this.memo = memo;
        this.contact = contact;
    }
}