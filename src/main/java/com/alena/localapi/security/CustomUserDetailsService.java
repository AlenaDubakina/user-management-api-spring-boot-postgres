package com.alena.localapi.security;

import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.exception.UserNotFoundException;
import com.alena.localapi.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден"));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + userEntity.getRole())
                ));
    }
}