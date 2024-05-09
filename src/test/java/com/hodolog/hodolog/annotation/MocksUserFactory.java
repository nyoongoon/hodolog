package com.hodolog.hodolog.annotation;

import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MocksUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {
    private final UserRepository userRepository;

    public MocksUserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        User user = User.builder()
                .email(annotation.username())
                //..
                .build();
        userRepository.save(user); // todo 커스텀 MockUser 기능 완성하기..

        return null;
    }
}
