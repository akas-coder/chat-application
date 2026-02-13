package com.chatapp.repository;

import com.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isOnline = :status, u.lastSeen = :lastSeen WHERE u.id = :userId")
    void updateUserOnlineStatus(Long userId, Boolean status, LocalDateTime lastSeen);
}