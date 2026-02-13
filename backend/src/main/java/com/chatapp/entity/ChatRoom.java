package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms", indexes = {
        @Index(name = "idx_room_name", columnList = "room_name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name", nullable = false, unique = true, length = 100)
    private String roomName; // e.g., "TECH_ROOM"

    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "chatRooms")
    private Set<User> members = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}