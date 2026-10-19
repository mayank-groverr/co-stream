package practice.mayank.co_stream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.co_stream.dto.room.RoomCreationRequestDto;
import practice.mayank.co_stream.dto.room.RoomMemberDto;
import practice.mayank.co_stream.dto.room.RoomResponseDto;
import practice.mayank.co_stream.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getJoinedRooms() {
        return ResponseEntity.ok(roomService.getJoinedRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> getJoinedRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.getJoinedRoom(roomId));
    }

    @GetMapping("/{roomId}/members")
    public ResponseEntity<Page<RoomMemberDto>> getRoomMembers(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(roomService.getRoomMembers(roomId, page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomCreationRequestDto roomRequestDto) {
        RoomResponseDto createdRoom = roomService.createAndJoinRoom(roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<RoomResponseDto> joinRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.joinRoomWithRoomId(roomId));
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<String> leaveRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.leaveRoom(roomId));
    }

    @DeleteMapping("/{roomId}/delete")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}