package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.hodolog.exception.InvalidSigninInformation;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.Login;
import com.hodolog.hodolog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
//        Session session = user.addSession();//연관관계를 통한 세션엔티티 생성 및 영속성 전이 (세션엔티티생성될떄randomUUID생성됨)
//        return session.getAccessToken();
        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        SCryptPasswordEncoder encoder =
                new SCryptPasswordEncoder(
                        16,
                        8,
                        1,
                        32,
                        64);

        String encryptedPassword = encoder.encode(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
