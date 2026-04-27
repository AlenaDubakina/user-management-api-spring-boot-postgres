package service;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import entity.UserEntity;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        UserEntity savedUser = userRepository.save(userEntity);
        return new UserResponseDTO(savedUser.getId(), savedUser.getEmail());
    }
}