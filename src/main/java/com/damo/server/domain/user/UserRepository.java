package com.damo.server.domain.user;

import com.damo.server.application.config.oauth.provider.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByProviderAndProviderId(ProviderType provider, String providerId);
}
