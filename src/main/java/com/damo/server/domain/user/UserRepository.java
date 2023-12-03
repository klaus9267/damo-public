package com.damo.server.domain.user;

import com.damo.server.application.config.oauth.provider.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderAndProviderId(ProviderType provider, String providerId);
}
