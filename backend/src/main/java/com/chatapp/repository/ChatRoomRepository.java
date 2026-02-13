package com.chatapp.repository;

import com.chatapp.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomName(String roomName);

    Boolean existsByRoomName(String roomName);
}