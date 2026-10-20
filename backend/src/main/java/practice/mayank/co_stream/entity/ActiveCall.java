package practice.mayank.co_stream.entity;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ActiveCall {

    private final String callId = UUID.randomUUID().toString();
    private final String roomId;
    private final CallType callType;

    @Getter(AccessLevel.NONE)
    private final Set<String> participants = ConcurrentHashMap.newKeySet();

    public ActiveCall(String roomId, CallType callType, String starter) {
        this.roomId = roomId;
        this.callType = callType;
        this.participants.add(starter);
    }

    public boolean addParticipant(String username) {
        return participants.add(username);
    }

    public boolean removeParticipant(String username) {
        return participants.remove(username);
    }

    public boolean hasParticipant(String username) {
        return participants.contains(username);
    }

    public List<String> getParticipants() {
        return List.copyOf(participants);
    }

    public boolean isEmpty() {
        return participants.isEmpty();
    }
}
