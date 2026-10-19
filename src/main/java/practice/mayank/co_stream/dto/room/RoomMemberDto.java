package practice.mayank.co_stream.dto.room;

import practice.mayank.co_stream.dto.user.UserResponse;
import practice.mayank.co_stream.entity.ActiveCall;
import practice.mayank.co_stream.entity.Membership;

public record RoomMemberDto(
        UserResponse user,
        Membership.Role role,
        ActiveCallDto activeCall
) {
}
