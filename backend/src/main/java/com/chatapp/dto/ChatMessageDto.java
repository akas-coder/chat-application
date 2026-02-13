package com.chatapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long id;
    private String content;
    private String senderUsername;
    private String recipientUsername;
    private String roomId;
    private String messageType;
    private LocalDateTime timestamp;

    // NEW fields
    private String imageUrl;
    private Boolean isImage = false;
    private Boolean isRead = false;
    private Boolean isDelivered = false;
    private Boolean isDeleted = false;
}