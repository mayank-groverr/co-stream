package practice.mayank.co_stream.mapper;

import org.mapstruct.Mapper;
import practice.mayank.co_stream.dto.user.UserRequest;
import practice.mayank.co_stream.dto.user.UserResponse;
import practice.mayank.co_stream.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    User userRequestToUser(UserRequest userRequest);
}
