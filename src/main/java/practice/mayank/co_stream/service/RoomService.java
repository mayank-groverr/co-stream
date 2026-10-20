package practice.mayank.co_stream.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.co_stream.dto.*;
import practice.mayank.co_stream.dto.room.RoomCreationRequestDto;
import practice.mayank.co_stream.dto.room.RoomMemberDto;
import practice.mayank.co_stream.dto.room.RoomResponseDto;
import practice.mayank.co_stream.entity.ActiveCall;
import practice.mayank.co_stream.entity.Room;
import practice.mayank.co_stream.entity.Membership;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.exception.customexceptions.AlreadyPresentException;
import practice.mayank.co_stream.exception.customexceptions.ResourceNotFoundException;
import practice.mayank.co_stream.mapper.RoomMapper;
import practice.mayank.co_stream.repository.MembershipRepository;
import practice.mayank.co_stream.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private static final int MAX_PAGE_SIZE = 50;
    private final RoomRepository roomRepository;
    private final MembershipRepository membershipRepository;
    private final MongoTemplateService mongoTemplateService;
    private final RoomMapper roomMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final SignalingService signalingService;


    @Transactional
    public RoomResponseDto createAndJoinRoom(RoomCreationRequestDto dto) {
        User user = UserService.getAuthenticatedUser();
        Room room = roomRepository.save(
                new Room(UUID.randomUUID().toString(), dto.roomName(), 1L));
        membershipRepository.save(
                new Membership(room.getRoomId(), user.getId(), Membership.Role.OWNER));
        return roomMapper.roomToRoomResponseDto(room);
    }


    @Transactional
    public RoomResponseDto joinRoomWithRoomId(String roomId) {

        User user = UserService.getAuthenticatedUser();
        Room room = findByRoomId(roomId);
        try {
            membershipRepository.save(new Membership(roomId, user.getId(), Membership.Role.MEMBER));
        } catch (DuplicateKeyException ignored) {
            throw new AlreadyPresentException("Already inside the room");
        }

        mongoTemplateService.runUpdateCriteria(
                Criteria.where("_id").is(room.getRoomId()),
                new Update().inc("totalMembers", 1),
                Room.class
        );
        room.setTotalMembers(room.getTotalMembers() + 1);
        return roomMapper.roomToRoomResponseDto(room);
    }

    @Transactional
    public String leaveRoom(String roomId) {
        User me = UserService.getAuthenticatedUser();
        Room room = findByRoomId(roomId);

        if (room.getTotalMembers() <= 1) {
            Long removed = membershipRepository.deleteByUserIdAndRoomId(me.getId(), roomId);
            if (removed == 0) {
                throw new ResourceNotFoundException("You're Not the member of this room");
            }
            roomRepository.deleteById(roomId);
            return "Left successfully";
        }

        boolean wasOwner = membershipRepository
                .existsByUserIdAndRoomIdAndRole(me.getId(), roomId, Membership.Role.OWNER);

        Long removed = membershipRepository.deleteByUserIdAndRoomId(me.getId(), roomId);
        if (removed == 0) {
            throw new ResourceNotFoundException("You're Not the member of this room");
        }

        mongoTemplateService.runUpdateCriteria(
                Criteria.where("_id").is(roomId),
                new Update().inc("totalMembers", -1),
                Room.class
        );

        if (wasOwner) {
            Membership next = mongoTemplateService.findRandomMember(roomId, null)
                    .orElseThrow(() -> new IllegalStateException("No member left to promote"));
            mongoTemplateService.runUpdateCriteria(
                    Criteria.where("userId").is(next.getUserId()).and("roomId").is(roomId),
                    new Update().set("role", Membership.Role.OWNER),
                    Membership.class
            );
        }

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                new SystemMessage(me.getUsername() + " left"));
        return "Left successfully";
    }


    @Transactional
    public String deleteRoom(String roomId){
        User authenticatedUser = UserService.getAuthenticatedUser();
        Room room = findByRoomId(roomId);
        applyDelete(room, authenticatedUser);
        return "Room Deleted";
    }


    private void applyDelete(Room room, User user) {
        if (!membershipRepository.existsByUserIdAndRoomIdAndRole(
                user.getId(), room.getRoomId(), Membership.Role.OWNER)) {
            throw new AccessDeniedException("Only Admin has right to delete room");
        }

        roomRepository.deleteById(room.getRoomId());
        membershipRepository.deleteByRoomId(room.getRoomId());
    }


    public RoomResponseDto getJoinedRoom(String roomId) {
        User user = UserService.getAuthenticatedUser();
        Room room = findByRoomId(roomId);
        if (!isUserPresentInRoom(user.getId(), roomId)) {
            throw new AccessDeniedException("Not a member of this room");
        }
        return roomMapper.roomToRoomResponseDto(room);
    }


    public List<RoomResponseDto> getJoinedRooms() {
        User me = UserService.getAuthenticatedUser();

        List<String> roomIds = membershipRepository.findByUserId(me.getId())
                .stream()
                .map(Membership::getRoomId)
                .toList();

        if (roomIds.isEmpty()) {
            return List.of();
        }

        return roomRepository.findAllByRoomIdIn(roomIds)
                .stream()
                .map(roomMapper::roomToRoomResponseDto)
                .toList();
    }


    public Page<RoomMemberDto> getRoomMembers(String roomId, int page, int size) {
        User authenticatedUser = UserService.getAuthenticatedUser();
        if (!isUserPresentInRoom(authenticatedUser.getId(), roomId)) {
            throw new AccessDeniedException("Not a member of this room");
        }

        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by("id"));

        Page<Membership> memberships = membershipRepository.findByRoomId(roomId, pageable);

        List<String> userIds = memberships
                .stream()
                .map(Membership::getUserId)
                .toList();

        Map<String, User> usersById = userService.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return memberships.map(m -> {
            User user = usersById.get(m.getUserId());
            return roomMapper.userToRoomMemberDto(user, m.getRole());
        });

    }

    public Room findByRoomId(String roomId) {
        Optional<Room> byRoomId = roomRepository.findByRoomId(roomId);
        return byRoomId.orElseThrow(
                () -> new ResourceNotFoundException("No room found having id : " + roomId)
        );
    }

    public boolean isUserPresentInRoom(String userId, String roomId) {
        return membershipRepository.existsByUserIdAndRoomId(userId, roomId);
    }

    private ActiveCall getRoomCall(String roomId){
         return signalingService.getCallsById().get(roomId);
    }

}
