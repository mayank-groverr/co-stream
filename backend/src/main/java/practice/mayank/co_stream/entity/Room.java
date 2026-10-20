package practice.mayank.co_stream.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "room")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"roomId"})
public class Room {


    @Id
    private String roomId;

    private String roomName;

    private Instant createdAt;

    private Long totalMembers;

    public Room(String roomId, String roomName, Long totalMembers) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.totalMembers = totalMembers;
        this.createdAt = Instant.now();
    }
}
