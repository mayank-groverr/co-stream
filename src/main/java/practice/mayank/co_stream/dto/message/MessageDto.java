package practice.mayank.co_stream.dto.message;

import java.time.Instant;

public record MessageDto(
        String senderId,
        String senderName,
        String roomID,
        String content,
        Instant sentAt

) {

}
