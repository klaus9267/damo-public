package com.damo.server.domain.oauth;

import com.damo.server.application.config.oauth.provider.OAuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthMemberRepository extends JpaRepository<OAuthMember, Long> {
    @Query("SELECT o FROM OAuthMember o WHERE o.oAuthId.oAuthProviderId = :oAuthProviderId AND o.oAuthId.oAuthProviderType = :oAuthProviderType")
    Optional<OAuthMember> findByOAuthId(@Param("oAuthProviderId") final String oAuthProviderId, @Param("oAuthProviderType") final OAuthProviderType oAuthProviderType);
}
