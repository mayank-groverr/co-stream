package practice.mayank.co_stream.dto.room;

import practice.mayank.co_stream.dto.message.MessageDto;
import practice.mayank.co_stream.dto.user.UserResponse;

import java.util.List;
import java.util.Set;

public record RoomResponseDto(
        String roomId,
        String roomName,
        UserResponse createdBy,
        long totalMembers
) {
}
