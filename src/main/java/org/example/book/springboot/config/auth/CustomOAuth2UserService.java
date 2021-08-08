package org.example.book.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.example.book.springboot.config.auth.dto.OAuthAttributes;
import org.example.book.springboot.config.auth.dto.SessionUser;
import org.example.book.springboot.domain.user.User;
import org.example.book.springboot.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.naming.directory.AttributeInUseException;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        //현재 로그인 진행 중인 서비스를 구분하는 코드(구글 로그인인지, 네이버 로그인인지..)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //OAuth2 로그인 진행 시 키가 되는 필드값. Primary Key 와 같은 의미.
        //구글의 경우 기본적으로 코드 지원(기본 코드 "sub")하지만, 네이버 카카오 등은 기본 지원하지 않음.
        //이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();

        //OAuthAttributes : OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 Dto 클래스.
        //이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용할 것.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                                            oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        //SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto 클래스.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());
    }

    public User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                        //이미 이메일이 존재하면 update.(소셜 사용자 정보가 업데이트 되었을 경우)
                        .map(entity -> entity.update(attributes.getName(),attributes.getPicture()))
                        //존재하지 않으면 save.(최초 가입)
                        .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
