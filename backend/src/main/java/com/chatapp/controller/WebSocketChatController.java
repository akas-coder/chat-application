package com.chatapp.controller;

import com.chatapp.dto.ChatMessageDto;
import com.chatapp.entity.ChatMessage;
import com.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * ROOM-BASED CHAT
     * Client sends to: /app/chat.sendToRoom
     * Server broadcasts to: /topic/room/{roomId}
     */
    @MessageMapping("/chat.sendToRoom")
    public void sendToRoom(@Payload ChatMessageDto messageDto) {
        try {
            // Save message to database
            ChatMessage savedMessage = chatService.saveRoomMessage(messageDto);

            // Convert to DTO
            ChatMessageDto response = convertToDto(savedMessage);

            // Broadcast to all subscribers of this room
            messagingTemplate.convertAndSend(
                    "/topic/room/" + messageDto.getRoomId(),
                    response
            );
        } catch (Exception e) {
            // Handle error (could send error message back to sender)
            System.err.println("Error sending room message: " + e.getMessage());
        }
    }


    /**
     * PRIVATE CHAT (Username-based)
     * Client sends to: /app/chat.sendToUser
     * Server sends to: /user/{recipientUsername}/queue/messages
     */
    /**
     * PRIVATE CHAT (Username-based)
     * Client sends to: /app/chat.sendToUser
     * Server sends to: /user/{recipientUsername}/queue/messages
     */
    @MessageMapping("/chat.sendToUser")
    public void sendToUser(@Payload ChatMessageDto messageDto) {
        try {
            System.out.println("=== PRIVATE MESSAGE DEBUG ===");
            System.out.println("From: " + messageDto.getSenderUsername());
            System.out.println("To: " + messageDto.getRecipientUsername());
            System.out.println("Content: " + messageDto.getContent());

            // Save message to database
            ChatMessage savedMessage = chatService.savePrivateMessage(messageDto);
            System.out.println("Message saved to database with ID: " + savedMessage.getId());

            // Convert to DTO
            ChatMessageDto response = convertToDto(savedMessage);

            // Send to recipient
            String recipientDestination = "/user/" + messageDto.getRecipientUsername() + "/queue/messages";
            messagingTemplate.convertAndSend(recipientDestination, response);
            System.out.println("Sent to recipient: " + recipientDestination);

            // Send to sender
            String senderDestination = "/user/" + messageDto.getSenderUsername() + "/queue/messages";
            messagingTemplate.convertAndSend(senderDestination, response);
            System.out.println("Sent to sender: " + senderDestination);

            System.out.println("=== MESSAGE SENT SUCCESSFULLY ===");

        } catch (Exception e) {
            System.err.println("Error sending private message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * JOIN ROOM notification
     * Client sends to: /app/chat.joinRoom/{roomId}
     */
    @MessageMapping("/chat.joinRoom/{roomId}")
    public void joinRoom(
            @DestinationVariable String roomId,
            @Payload ChatMessageDto messageDto) {

        messageDto.setRoomId(roomId);
        messageDto.setMessageType("JOIN");
        messageDto.setContent(messageDto.getSenderUsername() + " joined the room");

        // Broadcast join notification
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                messageDto
        );
    }

    /**
     * LEAVE ROOM notification
     */
    @MessageMapping("/chat.leaveRoom/{roomId}")
    public void leaveRoom(
            @DestinationVariable String roomId,
            @Payload ChatMessageDto messageDto) {

        messageDto.setRoomId(roomId);
        messageDto.setMessageType("LEAVE");
        messageDto.setContent(messageDto.getSenderUsername() + " left the room");

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                messageDto
        );
    }

    private ChatMessageDto convertToDto(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setContent(message.getContent());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setTimestamp(message.getCreatedAt());

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