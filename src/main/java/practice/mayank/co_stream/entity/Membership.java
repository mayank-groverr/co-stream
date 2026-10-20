package practice.mayank.co_stream.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "roomAndUserMembership")
@Getter
@Setter
@CompoundIndexes({
        @CompoundIndex(name = "user_room_unique", def = "{'userId': 1, 'roomId': 1}", unique = true),
        @CompoundIndex(name = "room_id_idx", def = "{'roomId': 1, '_id': -1}")
})
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    public enum Role{OWNER, MEMBER}


    @Id
    private String id;
    private String userId;
    private String roomId;
    private Role role;
    private Instant joinAt;

    public Membership(String roomId, String userId, Role role) {
        this.joinAt = Instant.now();
        this.role = role;
        this.roomId = roomId;
        this.userId = userId;
    }
}
