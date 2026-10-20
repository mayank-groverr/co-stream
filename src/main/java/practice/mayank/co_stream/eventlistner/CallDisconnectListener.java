package practice.mayank.co_stream.eventlistner;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import practice.mayank.co_stream.service.SignalingService;

import java.security.Principal;

@Component
public class CallDisconnectListener {

    private final SignalingService signalingService;

    public CallDisconnectListener(SignalingService signalingService) {
        this.signalingService = signalingService;
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            signalingService.handleDisconnect(user.getName());
        }
    }
}