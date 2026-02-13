package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages", indexes = {
        @Index(name = "idx_room_timestamp", columnList = "room_id, created_at"),
        @Index(name = "idx_sender_receiver", columnList = "sender_id, receiver_id, created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType;

    // NEW: For image messages
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_image", nullable = false)
    private Boolean isImage = false;

    // Message status
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "is_delivered", nullable = false)
    private Boolean isDelivered = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    // Soft delete
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING,
        IMAGE  // NEW
    }
}