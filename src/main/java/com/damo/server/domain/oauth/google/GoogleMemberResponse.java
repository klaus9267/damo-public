package com.damo.server.domain.oauth.google;

import com.damo.server.domain.oauth.OAuthId;
import com.damo.server.domain.oauth.OAuthMember;
import com.damo.server.domain.oauth.OAuthProviderType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String email,
        Boolean verifiedEmail,
        String name,
        String givenName,
        String familyName,
        String picture,
        String locale
) {
    public OAuthMember toDomain() {
        return OAuthMember.builder()
                .oAuthId(new OAuthId(String.valueOf(id), OAuthProviderType.GOOGLE))
                .nickname(name)
                .profileImageUrl(picture)
                .build();
    }
}