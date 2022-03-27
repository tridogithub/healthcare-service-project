package com.trido.healthcare.repository;

import com.trido.healthcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndIsDeleted(String username, boolean isDeleted);

    Optional<User> findByUsernameAndIsDeletedAndEnabled(String username, boolean isDeleted, boolean isEnabled);

    Optional<User> findByIdAndIsDeleted(UUID id, boolean isDeleted);

    List<User> findAllByIsDeleted(boolean isDeleted);

    boolean existsByUsername(String username);
}
