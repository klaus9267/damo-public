package com.damo.server.domain.oauth.naver;

import com.damo.server.domain.oauth.OAuthId;
import com.damo.server.domain.oauth.OAuthMember;
import com.damo.server.domain.oauth.OAuthProviderType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultCode,
        String message,
        Response response
) {

    public OAuthMember toDomain() {
        return OAuthMember.builder()
                .oAuthId(new OAuthId(String.valueOf(response.id), OAuthProviderType.NAVER))
                .nickname(response.nickname)
                .profileImageUrl(response.profileImage)
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
