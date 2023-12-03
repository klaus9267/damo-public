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

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Provider oAuth2Provider = null;

        // TODO: 디자인 패턴 적용으로 가독성 개선
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2Provider = new OAuth2Google(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2Provider = new OAuth2Naver(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2Provider = new OAuth2Kakao(oAuth2User.getAttributes());
        } else {
            System.out.println("구글과 네이버와 카카오 가능");
        }
        assert oAuth2Provider != null;

        ProviderType provider = oAuth2Provider.getProvider();
        String providerId = oAuth2Provider.getProviderId();
        String username = provider + "_" + providerId;
        String name = oAuth2Provider.getName();
        String email = oAuth2Provider.getEmail();

        User userEntity = userRepository.findByProviderAndProviderId(provider, providerId);
        if (userEntity == null) {
            userEntity = User
                    .builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .role(UserRole.USER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(oAuth2User.getAttributes(), username);
    }
}
