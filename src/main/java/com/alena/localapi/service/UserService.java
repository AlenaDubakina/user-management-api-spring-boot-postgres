package com.alena.localapi.service;

import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.exception.UserAlreadyExistsException;
import com.alena.localapi.exception.UserNotFoundException;
import com.alena.localapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> new UserResponseDTO(userEntity.getId(), userEntity.getEmail()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        UserEntity userEntityById = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким id %d не существует"
                        .formatted(id)));

        return new UserResponseDTO(userEntityById.getId(), userEntityById.getEmail());
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