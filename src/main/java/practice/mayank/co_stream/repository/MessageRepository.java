package practice.mayank.co_stream.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.co_stream.entity.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
