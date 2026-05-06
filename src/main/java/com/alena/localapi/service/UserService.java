package com.alena.localapi.service;

import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.entity.UserEntity;
import com.alena.localapi.exception.UserAlreadyExistsException;
import com.alena.localapi.exception.UserEmptyFieldException;
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

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id %d не существует"
                .formatted(id)));

        userRepository.delete(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(("Пользователь с таким id %d не существует"
                .formatted(id))));

        if (userRepository.existsByEmail(userRequestDTO.getEmail()) &&
                !existingUser.getEmail().equals(userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email %s уже существует"
                    .formatted(userRequestDTO.getEmail()));
        }

        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(userRequestDTO.getPassword());

        UserEntity updateUserEntity = userRepository.save(existingUser);

        return new UserResponseDTO(updateUserEntity.getId(), updateUserEntity.getEmail());
    }

    public UserResponseDTO patchUpdateUser(Long id, UserPatchDTO patchUpdateUser) {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(("Пользователь с таким id %d не существует"
                .formatted(id))));

        if (patchUpdateUser.getEmail() != null) {

            if (patchUpdateUser.getEmail().isBlank()) {
                throw new UserEmptyFieldException("email", "Поле не должно быть пустым");
            }

            if (userRepository.existsByEmail(patchUpdateUser.getEmail()) && !existingUser.getEmail().equals(patchUpdateUser.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с таким email %s уже существует"
                        .formatted(patchUpdateUser.getEmail()));
            }

            existingUser.setEmail(patchUpdateUser.getEmail());
        }

        if (patchUpdateUser.getPassword() != null) {

            if (patchUpdateUser.getPassword().isBlank()) {
                throw new UserEmptyFieldException("password", "Поле не должно быть пустым");
            }

            existingUser.setPassword(patchUpdateUser.getPassword());
        }

        UserEntity updateUserEntity = userRepository.save(existingUser);

        return new UserResponseDTO(updateUserEntity.getId(), updateUserEntity.getEmail());
    }
}