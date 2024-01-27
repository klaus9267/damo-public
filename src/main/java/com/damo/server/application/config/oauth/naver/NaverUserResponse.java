package com.damo.server.application.config.oauth.naver;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import com.damo.server.domain.user.UserRole;
import com.damo.server.domain.user.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverUserResponse(
        String resultCode,
        String message,
        Response response
) {

    public User toDomain() {
        return User.builder()
                .name(response.name)
                .email(response.email)
                .role(UserRole.USER)
                .username(response.id + "_" + OAuthProviderType.KAKAO.getKey())
                .providerId(response.id)
                .providerType(OAuthProviderType.NAVER)
                .profileUrl(response.profileImage)
                .build();
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Response(
            String id,
            String nickname,
            String name,
            String email,
            String gender,
            String age,
            String birthday,
            String profileImage,
            String birthyear,
            String mobile
    ) {
    }
}
