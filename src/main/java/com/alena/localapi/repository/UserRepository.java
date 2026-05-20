package com.alena.localapi.repository;

import com.alena.localapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    boolean existsById(Long id);

    Optional<UserEntity> findByEmail(String email);
}