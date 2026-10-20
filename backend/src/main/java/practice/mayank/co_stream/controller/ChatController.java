package practice.mayank.co_stream.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import practice.mayank.co_stream.dto.message.MessageDto;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.service.MessageService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/sendMessage/{roomId}")
    public void sendMessage(
            @Payload MessageDto messageDto,
            @DestinationVariable String roomId,
            Principal principal
    ){
        User sender = (User) ((Authentication) principal).getPrincipal();
        System.out.println(sender.getId());
         messageService.sendMessage(sender, messageDto, roomId);
    }



}
