package com.damo.server.application.controller;

import com.damo.server.application.security.OAuthService;
import com.damo.server.application.security.provider.OAuthProviderType;
import com.damo.server.common.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockCustomUser
public class OAuthControllerTest {
  @MockBean
  OAuthService oAuthService;
  @Autowired
  MockMvc mvc;
  private final String END_POINT = "/oauth";
  private final ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Nested
  @DisplayName("성공 케이스")
  class 성공 {
    @Test
    void OAuth_로그인_페이지_요청() throws Exception {
      final String redirectUrl = "http://localhost:3000/oauth/test-url/";

      for(final OAuthProviderType providerType : OAuthProviderType.values()) {
        when(oAuthService.getAuthCodeRequestUrl(eq(providerType), anyBoolean())).thenReturn(redirectUrl + providerType.getKey());

        mvc.perform(get(END_POINT + "/" + providerType.getKey())
            .contentType(MediaType.APPLICATION_JSON)
          ).andDo(print())
          .andExpect(status().isFound())
          .andExpect(header().string("Location", redirectUrl + providerType.getKey()));
      }
    }
  }
}
