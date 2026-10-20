package practice.mayank.co_stream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.co_stream.dto.message.MessagePageDto;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.service.MessageService;
import practice.mayank.co_stream.service.UserService;


@RestController
@RequestMapping("/api/v1/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<MessagePageDto> getMessages(
            @PathVariable String roomId,
            @RequestParam(required = false) String before,
            @RequestParam(required = false) String after,
            @RequestParam(required = false) Integer limit
    ) {
        User authenticatedUser = UserService.getAuthenticatedUser();
        return ResponseEntity.ok(messageService.getMessages(authenticatedUser, roomId, before, after, limit));
    }
}