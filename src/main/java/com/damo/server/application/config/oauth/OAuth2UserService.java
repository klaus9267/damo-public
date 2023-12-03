package com.damo.server.application.config.oauth;

import com.damo.server.application.config.oauth.provider.OAuth2Google;
import com.damo.server.application.config.oauth.provider.OAuth2Kakao;
import com.damo.server.application.config.oauth.provider.OAuth2Naver;
import com.damo.server.application.config.oauth.provider.OAuth2Provider;
import com.damo.server.application.config.oauth.provider.ProviderType;
import com.damo.server.domain.user.User;
import com.damo.server.domain.user.UserRepository;
import com.damo.server.domain.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        final OAuth2Provider oAuth2Provider = getOAuth2Provider(userRequest, attributes);

        final String username = oAuth2Provider.getProvider() + "_" + oAuth2Provider.getProviderId();

        if(userRepository.existsByProviderAndProviderId(oAuth2Provider.getProvider(), oAuth2Provider.getProviderId())) {
            saveUser(oAuth2Provider, username);
        }

        return new PrincipalDetails(attributes, username);
    }

    private void saveUser(OAuth2Provider provider, String username) {
        userRepository.save(
            User.builder()
                .username(username)
                .name(provider.getName())
                .email(provider.getEmail())
                .role(UserRole.USER)
                .provider(provider.getProvider())
                .providerId(provider.getProviderId())
                .build()
        );
    }

    private OAuth2Provider getOAuth2Provider(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Provider oAuth2Provider = registrationId.equals("google")
                ? new OAuth2Google(attributes)
                : registrationId.equals("naver")
                ? new OAuth2Naver(attributes)
                : registrationId.equals("kakao")
                ? new OAuth2Kakao(attributes)
                : null;

        assert oAuth2Provider != null;
        return oAuth2Provider;
    }
}
