package com.alena.localapi.controller;

import com.alena.localapi.dto.UserPatchDTO;
import com.alena.localapi.dto.UserRequestDTO;
import com.alena.localapi.dto.UserResponseDTO;
import com.alena.localapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public UserResponseDTO putUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(id, userRequestDTO);
    }

    @PatchMapping("/{id}")
    public UserResponseDTO patchUser(@PathVariable Long id, @Valid @RequestBody UserPatchDTO patchUpdateUser) {
        return userService.patchUpdateUser(id, patchUpdateUser);
    }
}