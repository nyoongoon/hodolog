package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.Session;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.InvalidSigninInformation;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public String signin(Login request){
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = user.addSession();//연관관계를 통한 세션엔티티 생성 및 영속성 전이 (세션엔티티생성될떄randomUUID생성됨)
        return session.getAccessToken();
    }
}
