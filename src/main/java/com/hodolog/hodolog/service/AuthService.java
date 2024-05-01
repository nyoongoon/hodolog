package com.hodolog.hodolog.service;

import com.hodolog.hodolog.crypto.PasswordEncoder;
import com.hodolog.hodolog.crypto.ScryptPasswordEncoder;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.AlreadyExistsEmailException;
import com.hodolog.hodolog.exception.InvalidSigninInformation;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.Login;
import com.hodolog.hodolog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public Long signin(Login login) {
//        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(InvalidSigninInformation::new);
//        Session session = user.addSession();//연관관계를 통한 세션엔티티 생성 및 영속성 전이 (세션엔티티생성될떄randomUUID생성됨)
//        return session.getAccessToken();
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);
        boolean matches = encoder.matches(login.getPassword(), user.getPassword());
        if(!matches){
            throw  new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }
        String encryptedPassword = encoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
