package com.alena.localapi.auth.service;

import com.alena.localapi.auth.dto.AuthResponseDTO;
import com.alena.localapi.auth.dto.LoginRequestDTO;
import com.alena.localapi.auth.dto.RegisterRequestDTO;
import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.exception.UserAlreadyExistsException;
import com.alena.localapi.repository.UserRepository;
import com.alena.localapi.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email %s уже существует"
                    .formatted(dto.getEmail()));
        }

        UserEntity userEntity = new UserEntity(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        userRepository.save(userEntity);

        return new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail()));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Неверные учетные данные"));

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("Неверные учетные данные");
        }

        return new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail()));
    }
}