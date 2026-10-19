package practice.mayank.co_stream.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "message")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    private String id;

    private String senderId;
    private String senderName;
    private String roomId;
    private String content;
    private Instant sentAt;


    public Message(String content, String roomId, String senderName, String senderId) {
        this.sentAt = Instant.now();
        this.content = content;
        this.roomId = roomId;
        this.senderName = senderName;
        this.senderId = senderId;
    }
}
