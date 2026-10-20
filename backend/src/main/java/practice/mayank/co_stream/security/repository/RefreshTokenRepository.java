package practice.mayank.co_stream.security.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.co_stream.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
    Optional<RefreshToken> findByToken(String token);
}
