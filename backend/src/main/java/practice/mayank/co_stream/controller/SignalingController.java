package practice.mayank.co_stream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import practice.mayank.co_stream.dto.signal.SignalMessage;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.service.SignalingService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SignalingController {

    private final SignalingService signalingService;


    @MessageMapping("/call.signal")
    public void signal(SignalMessage message, Principal principal) {
        User sender = (User) ((Authentication) principal).getPrincipal();
        signalingService.handle(message, sender);
    }

    // error text goes only to the sender: /user/queue/errors
    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public String handleError(IllegalArgumentException ex) {
        return ex.getMessage();
    }

}
