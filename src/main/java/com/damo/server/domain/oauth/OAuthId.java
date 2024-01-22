package com.damo.server.domain.oauth;

import com.damo.server.application.config.new_oauth.provider.OAuthProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthId {
    @Column(nullable = false, name = "o_auth_provider_id")
    private String oAuthProviderId;

    @Column(nullable = false, name = "o_auth_provider_type")
    @Enumerated(EnumType.STRING)
    private OAuthProviderType oAuthProviderType;
}
