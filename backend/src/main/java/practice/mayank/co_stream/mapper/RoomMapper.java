package practice.mayank.co_stream.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import practice.mayank.co_stream.dto.room.RoomMemberDto;
import practice.mayank.co_stream.dto.room.RoomResponseDto;
import practice.mayank.co_stream.entity.Membership;
import practice.mayank.co_stream.entity.Room;
import practice.mayank.co_stream.entity.User;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, RoomMapper.class})
public interface RoomMapper {

    RoomResponseDto roomToRoomResponseDto(Room room);

    @Mapping(source = "role", target = "role")
    RoomMemberDto userToRoomMemberDto(User user, Membership.Role role);



}
