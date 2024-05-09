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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test") // test프로파일로 실행
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
//        assertEquals("1234", user.getPassword()); // todo 비밀번호 테스트 방안
        assertEquals("hodolman", user.getName());
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

    /**
     * 시큐리티 도입으로 불필요
     */
//    @Test
//    @DisplayName("로그인 성공")
//    void test3() {
//        // given
//        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
//        String encryptedPassword = encoder.encrypt("1234");
//        String email = "hodolman88@gmail.com";
//        User user = User.builder()
//                .email(email)
//                .name("짱돌맨")
//                .password(encryptedPassword)
//                .build();
//        userRepository.save(user);
//
//        Login login = Login.builder()
//                .email(email)
//                .password("1234")
//                .build();
//        // when
//        Long userId = authService.signin(login);
//
//        // then
//        assertNotNull(userId);
//    }
//
//    @Test
//    @DisplayName("로그인 비밀번호 틀림")
//    void test4() {
//        // given
//        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
//        String encryptedPassword = encoder.encrypt("1234");
//        String email = "hodolman88@gmail.com";
//        User user = User.builder()
//                .email(email)
//                .name("짱돌맨")
//                .password(encryptedPassword)
//                .build();
//        userRepository.save(user);
//
//        Login login = Login.builder()
//                .email("hodolman88@gmail.com")
//                .password("4567")
//                .build();
//        // expected
//        assertThrows(InvalidSigninInformation.class, ()-> authService.signin(login));
//    }
}