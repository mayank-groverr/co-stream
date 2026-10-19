package practice.mayank.co_stream.security.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role")
@Getter
@Setter
public class Role {

    @Id
    private ObjectId roleId;

    private RoleEnum roleName;
}
