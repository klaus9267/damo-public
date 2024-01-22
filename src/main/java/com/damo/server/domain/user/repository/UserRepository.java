package com.damo.server.domain.user.repository;

import com.damo.server.application.config.oauth.provider.ProviderType;
import com.damo.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByProviderAndProviderId(ProviderType provider, String providerId);
    Optional<User> findOneByProviderId(final String providerId);
}
