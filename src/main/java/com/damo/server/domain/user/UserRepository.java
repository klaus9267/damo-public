package com.damo.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByProviderAndProviderId(String provider, String providerId);
}
