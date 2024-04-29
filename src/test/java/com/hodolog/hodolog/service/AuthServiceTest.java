package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .email("hodolman88@gmail.com")
                .name("hodolman")
                .password("1234")
                .build();
        // when
        authService.signup(signup);
        // then
        assertEquals(userRepository.count(), 1L);
        User user = userRepository.findAll().iterator().next();
        assertEquals(signup.getEmail(), user.getEmail());
        assertNotNull(user.getPassword());
        assertNotEquals("1234", user.getPassword());
        assertEquals("hodolman", user.getName() );
    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일")
    void test2() {
        // given
        String email = "hodolman88@gmail.com";
        User user = User.builder()
                .email(email)
                .name("짱돌맨")
                .password("1234")
                .build();
        userRepository.save(user);
        Signup signup = Signup.builder()
                .email(email)
                .name("hodolman")
                .password("1234")
                .build();
        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
}