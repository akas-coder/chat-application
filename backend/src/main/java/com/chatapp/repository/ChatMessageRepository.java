package com.chatapp.repository;

import com.chatapp.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Get all messages in a room, ordered by time
    @Query("SELECT m FROM ChatMessage m WHERE m.room.id = :roomId ORDER BY m.createdAt ASC")
    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    // Get private messages between two users
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findPrivateMessagesBetweenUsers(Long userId1, Long userId2);

    // Count unread messages for a user
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver.id = :userId AND m.isRead = false")
    Long countUnreadMessages(Long userId);
}