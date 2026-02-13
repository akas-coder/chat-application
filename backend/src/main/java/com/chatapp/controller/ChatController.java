package com.chatapp.controller;

import com.chatapp.dto.ChatMessageDto;
import com.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Get all messages from a specific room
     */
    @GetMapping("/room/{roomName}/messages")
    public ResponseEntity<List<ChatMessageDto>> getRoomMessages(
            @PathVariable String roomName) {
        try {
            List<ChatMessageDto> messages = chatService.getRoomMessages(roomName);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get private messages between two users
     */
    @GetMapping("/private/{username1}/{username2}/messages")
    public ResponseEntity<List<ChatMessageDto>> getPrivateMessages(
            @PathVariable String username1,
            @PathVariable String username2) {
        try {
            List<ChatMessageDto> messages =
                    chatService.getPrivateMessages(username1, username2);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a message (soft delete)
     */
    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam String username) {
        try {
            chatService.deleteMessage(messageId, username);
            return ResponseEntity.ok(Map.of("message", "Message deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * Mark message as read
     */
    @PutMapping("/message/{messageId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long messageId) {
        try {
            chatService.markAsRead(messageId);
            return ResponseEntity.ok(Map.of("message", "Marked as read"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * Get user's last seen
     */
    @GetMapping("/user/{username}/last-seen")
    public ResponseEntity<?> getLastSeen(@PathVariable String username) {
        try {
            Map<String, Object> lastSeen = chatService.getUserLastSeen(username);
            return ResponseEntity.ok(lastSeen);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}