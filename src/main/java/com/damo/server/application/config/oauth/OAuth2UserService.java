package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuth2Provider;
import com.damo.server.domain.user.User;
import com.damo.server.domain.user.UserRepository;
import com.damo.server.domain.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        final OAuth2ProviderFactory providerFactory = new OAuth2ProviderFactory();
        final OAuth2Provider oAuth2Provider = providerFactory.getOAuth2Provider(userRequest.getClientRegistration().getRegistrationId(), attributes);

        if(userRepository.existsByProviderAndProviderId(oAuth2Provider.getProvider(), oAuth2Provider.getProviderId())) {
            userRepository.save(
                    User.builder()
                            .username(oAuth2Provider.getUsername())
                            .name(oAuth2Provider.getName())
                            .email(oAuth2Provider.getEmail())
                            .role(UserRole.USER)
                            .provider(oAuth2Provider.getProvider())
                            .providerId(oAuth2Provider.getProviderId())
                            .build()
            );
        }

        return new PrincipalDetails(attributes, oAuth2Provider.getUsername());
    }
}
