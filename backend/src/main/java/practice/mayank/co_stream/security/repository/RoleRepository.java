package practice.mayank.co_stream.security.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.co_stream.security.entity.Role;
import practice.mayank.co_stream.security.entity.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Optional<Role> findByRoleName(RoleEnum roleName);
}
