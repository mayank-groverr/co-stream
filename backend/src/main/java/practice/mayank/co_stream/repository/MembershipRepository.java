package practice.mayank.co_stream.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.co_stream.entity.Membership;

import java.util.Optional;

public interface MembershipRepository extends MongoRepository<Membership, String> {
    Long deleteByUserIdAndRoomId(String userID, String roomID);

    void deleteByRoomId(String roomId);

    boolean existsByUserIdAndRoomId(String userId, String roomId);

    Optional<Membership> findByUserId(String id);

    Page<Membership> findByRoomId(String roomId, Pageable pageable);

    boolean existsByUserIdAndRoomIdAndRole(String id, String roomId, Membership.Role role);
}
