package practice.mayank.co_stream.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import practice.mayank.co_stream.dto.signal.SignalMessage;
import practice.mayank.co_stream.entity.ActiveCall;
import practice.mayank.co_stream.entity.SignalType;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.repository.MembershipRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Getter
public class SignalingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MembershipRepository membershipRepository;

    private final Map<String, ActiveCall> callsById = new ConcurrentHashMap<>();
    private final Map<String, String> callIdByRoom = new ConcurrentHashMap<>();


    public void handle(SignalMessage message, User sender) {
        if (message.type() == null) {
            throw new IllegalArgumentException("type is required");
        }
        switch (message.type()) {
            case CALL_START -> startCall(message, sender.getId());
            case JOIN -> joinCall(message, sender.getId());
            case OFFER, ANSWER, ICE_CANDIDATE -> relay(message, sender.getId());
            case LEAVE -> leaveCall(message.callId(), sender.getId());
            default -> throw new IllegalArgumentException("unsupported type: " + message.type());
        }
    }


    private void startCall(SignalMessage message, String sender) {
        if (message.roomId() == null || message.callType() == null) {
            throw new IllegalArgumentException("roomId and callType are required");
        }
        if (!membershipRepository.existsByUserIdAndRoomId( sender, message.roomId())) {
            throw new IllegalArgumentException("not a member of this room");
        }

        ActiveCall call = new ActiveCall(message.roomId(), message.callType(), sender);


        if (callIdByRoom.putIfAbsent(call.getRoomId(), call.getCallId()) != null) {
            throw new IllegalArgumentException("a call is already active in this room");
        }
        callsById.put(call.getCallId(), call);

        SignalMessage announcement = new SignalMessage(
                SignalType.CALL_STARTED,
                call.getCallId(),
                call.getRoomId(),
                sender,
                null,
                call.getCallType(),
                null
        );
        messagingTemplate.convertAndSend("/topic/room/" + call.getRoomId() + "/call", announcement);
    }

    private void joinCall(SignalMessage message, String sender) {
        ActiveCall call = findCall(message.callId());

        if (!membershipRepository.existsByUserIdAndRoomId(sender , call.getRoomId())) { // same placeholder as Step 5
            throw new IllegalArgumentException("not a member of this room");
        }

        // snapshot BEFORE adding, so the joiner is not in his own list
        List<String> existing = call.getParticipants();

        if (!call.addParticipant(sender)) {
            throw new IllegalArgumentException("already in this call");
        }

        // 1. tell the joiner who is already here
        sendToUser(sender, new SignalMessage(
                SignalType.JOINED, call.getCallId(), call.getRoomId(),
                null, sender, call.getCallType(), existing));

        // 2. tell each existing participant that someone joined
        for (String participant : existing) {
            sendToUser(participant, new SignalMessage(
                    SignalType.PEER_JOINED, call.getCallId(), call.getRoomId(),
                    sender, participant, call.getCallType(), null));
        }
    }

    private void relay(SignalMessage message, String sender) {
        ActiveCall call = findCall(message.callId());

        if (message.to() == null || message.to().equals(sender)) {
            throw new IllegalArgumentException("valid target required");
        }
        if (!call.hasParticipant(sender) || !call.hasParticipant(message.to())) {
            throw new IllegalArgumentException("both users must be in the call");
        }

        sendToUser(message.to(), new SignalMessage(
                message.type(), call.getCallId(), call.getRoomId(),
                sender, message.to(), call.getCallType(), message.payload()));
    }

    private void leaveCall(String callId, String username) {
        ActiveCall call = findCall(callId);

        if (!call.removeParticipant(username)) {
            throw new IllegalArgumentException("not in this call");
        }

        for (String participant : call.getParticipants()) {
            sendToUser(participant, new SignalMessage(
                    SignalType.PEER_LEFT, call.getCallId(), call.getRoomId(),
                    username, participant, call.getCallType(), null));
        }

        if (call.isEmpty()) {
            endCall(call);
        }
    }

    private void endCall(ActiveCall call) {
        // remove() returns null if someone already ended it, so CALL_ENDED is sent once
        if (callsById.remove(call.getCallId()) == null) {
            return;
        }
        callIdByRoom.remove(call.getRoomId(), call.getCallId());

        messagingTemplate.convertAndSend(
                "/topic/room/" + call.getRoomId() + "/call",
                new SignalMessage(SignalType.CALL_ENDED, call.getCallId(), call.getRoomId(),
                        null, null, call.getCallType(), null));
    }

    private ActiveCall findCall(String callId) {
        ActiveCall call = callId == null ? null : callsById.get(callId);
        if (call == null) {
            throw new IllegalArgumentException("call not found");
        }
        return call;
    }

    private void sendToUser(String username, SignalMessage message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/call", message);
    }

    public void handleDisconnect(String username) {
        for (ActiveCall call : callsById.values()) {
            if (call.hasParticipant(username)) {
                leaveCall(call.getCallId(), username);
            }
        }
    }
}
