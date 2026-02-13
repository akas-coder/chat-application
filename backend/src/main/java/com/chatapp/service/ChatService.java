package com.chatapp.service;

import com.chatapp.dto.ChatMessageDto;
import com.chatapp.entity.ChatMessage;
import com.chatapp.entity.ChatRoom;
import com.chatapp.entity.User;
import com.chatapp.repository.ChatMessageRepository;
import com.chatapp.repository.ChatRoomRepository;
import com.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessage saveRoomMessage(ChatMessageDto dto) {
        User sender = userRepository.findByUsername(dto.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatRoom room = roomRepository.findByRoomName(dto.getRoomId())
                .orElseGet(() -> createRoom(dto.getRoomId()));

        if (!room.getMembers().contains(sender)) {
            room.getMembers().add(sender);
            roomRepository.save(room);
        }

        ChatMessage message = new ChatMessage();
        message.setContent(dto.getContent());
        message.setSender(sender);
        message.setRoom(room);
        message.setMessageType(ChatMessage.MessageType.CHAT);

        // Handle image messages
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            message.setImageUrl(dto.getImageUrl());
            message.setIsImage(true);
            message.setMessageType(ChatMessage.MessageType.IMAGE);
        }

        return messageRepository.save(message);
    }

    @Transactional
    public ChatMessage savePrivateMessage(ChatMessageDto dto) {
        User sender = userRepository.findByUsername(dto.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByUsername(dto.getRecipientUsername())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatMessage message = new ChatMessage();
        message.setContent(dto.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageType(ChatMessage.MessageType.CHAT);
        message.setIsDelivered(true);
        message.setDeliveredAt(LocalDateTime.now());

        // Handle image messages
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            message.setImageUrl(dto.getImageUrl());
            message.setIsImage(true);
            message.setMessageType(ChatMessage.MessageType.IMAGE);
        }

        return messageRepository.save(message);
    }

    public List<ChatMessageDto> getRoomMessages(String roomName) {
        ChatRoom room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return messageRepository.findByRoomIdOrderByCreatedAtAsc(room.getId())
                .stream()
                .filter(msg -> !msg.getIsDeleted()) // Exclude deleted messages
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChatMessageDto> getPrivateMessages(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findByUsername(username2)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findPrivateMessagesBetweenUsers(user1.getId(), user2.getId())
                .stream()
                .filter(msg -> !msg.getIsDeleted()) // Exclude deleted messages
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(Long messageId, String username) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Only sender can delete
        if (!message.getSender().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this message");
        }

        message.setIsDeleted(true);
        message.setDeletedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setIsRead(true);
        message.setReadAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    public Map<String, Object> getUserLastSeen(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("isOnline", user.getIsOnline());
        response.put("lastSeen", user.getLastSeen());

        return response;
    }

    private ChatRoom createRoom(String roomName) {
        ChatRoom room = new ChatRoom();
        room.setRoomName(roomName);
        return roomRepository.save(room);
    }

    private ChatMessageDto convertToDto(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setTimestamp(message.getCreatedAt());
        dto.setIsRead(message.getIsRead());
        dto.setIsDelivered(message.getIsDelivered());
        dto.setIsDeleted(message.getIsDeleted());

        // Image support
        dto.setImageUrl(message.getImageUrl());
        dto.setIsImage(message.getIsImage());

        if (message.getReceiver() != null) {
            dto.setRecipientUsername(message.getReceiver().getUsername());
            dto.setMessageType("PRIVATE");
        } else if (message.getRoom() != null) {
            dto.setRoomId(message.getRoom().getRoomName());
            dto.setMessageType("ROOM");
        }

        return dto;
    }
}