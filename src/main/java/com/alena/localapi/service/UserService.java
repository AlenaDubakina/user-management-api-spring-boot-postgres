package com.alena.localapi.service;

import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.exception.UserAlreadyExistsException;
import com.alena.localapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email %s уже существует"
                    .formatted(userRequestDTO.getEmail()));
        }

        UserEntity userEntity = new UserEntity(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        UserEntity savedUser = userRepository.save(userEntity);

        return new UserResponseDTO(savedUser.getId(), savedUser.getEmail());
    }
}