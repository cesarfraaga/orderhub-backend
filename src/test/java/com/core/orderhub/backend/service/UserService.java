package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.User;
import com.core.orderhub.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired //TODO: É melhor injeção por construtor
    PasswordEncoder passwordEncoder;

    @Autowired //TODO: É melhor injeção por construtor
    UserRepository userRepository;

    public void createUser(String email, String password) {
        User user = new User(email, passwordEncoder.encode(password));

        userRepository.save(user);
    }

}
