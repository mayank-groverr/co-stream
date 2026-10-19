package practice.mayank.co_stream.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import practice.mayank.co_stream.entity.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByRoomId(String roomId);

    Optional<Room> findAllByRoomIdIn(List<String> roomIds);
}
