package practice.mayank.co_stream.security.entity;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import practice.mayank.co_stream.entity.User;

import java.time.Instant;


@Document(collection = "refresh_tokens")
@Getter
@Setter
public class RefreshToken {


    @Id
    private ObjectId id;

    private String token;

    private User user;

    private Instant expiryTime;
}
