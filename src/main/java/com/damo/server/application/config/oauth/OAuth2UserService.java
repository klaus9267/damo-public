package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuth2Provider;
import com.damo.server.domain.user.entity.User;
import com.damo.server.domain.user.dto.UserDto;
import com.damo.server.domain.user.repository.UserRepository;
import com.damo.server.domain.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@AllArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        final Map<String, Object> attributes = oAuth2User.getAttributes();

        final OAuth2ProviderFactory providerFactory = new OAuth2ProviderFactory();
        final OAuth2Provider oAuth2Provider = providerFactory.getOAuth2Provider(userRequest.getClientRegistration().getRegistrationId(), attributes);

        validateEmail(oAuth2Provider.getEmail());
        User user = registerUser(oAuth2Provider);

        return new PrincipalDetails(UserDto.from(user), attributes);
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }
    }

    private User registerUser(OAuth2Provider oAuth2Provider) {
        return userRepository.findOneByProviderAndProviderId(oAuth2Provider.getProvider(), oAuth2Provider.getProviderId()).orElseGet(() -> userRepository.save(
                User.builder()
                        .username(oAuth2Provider.getUsername())
                        .name(oAuth2Provider.getName())
                        .email(oAuth2Provider.getEmail())
                        .role(UserRole.USER)
                        .provider(oAuth2Provider.getProvider())
                        .providerId(oAuth2Provider.getProviderId())
                        .build())
        );
    }
}
