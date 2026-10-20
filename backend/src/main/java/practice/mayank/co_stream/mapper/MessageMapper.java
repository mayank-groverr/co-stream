package practice.mayank.co_stream.mapper;

import org.mapstruct.Mapper;
import practice.mayank.co_stream.dto.message.MessageDto;
import practice.mayank.co_stream.entity.Message;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {

    Message messageDtoToMessage(MessageDto messageDto);

    MessageDto messageTomessageDto(Message message);
}
