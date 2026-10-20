package practice.mayank.co_stream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import practice.mayank.co_stream.dto.KeysetPage;
import practice.mayank.co_stream.dto.message.MessageDto;
import practice.mayank.co_stream.dto.message.MessagePageDto;
import practice.mayank.co_stream.entity.Message;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.mapper.MessageMapper;
import practice.mayank.co_stream.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final int MAX_CONTENT_LENGTH = 500;
    private static final int DEFAULT_PAGE_SIZE = 60;
    private static final int MAX_PAGE_SIZE = 100;


    private final RoomService roomService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoTemplateService mongoTemplateService;

    public MessageDto sendMessage(User sender, MessageDto request, String roomId) {
        String content = request.content() == null ? "" : request.content().strip();

        if (content.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Message too long (max " + MAX_CONTENT_LENGTH + " characters)");
        }

        boolean isMember =
                roomService.isUserPresentInRoom(sender.getId(), roomId);

        System.out.println("Is member: " + isMember);

        if (!isMember) {
            throw new AccessDeniedException("You are not a member of this room");
        }

        Message savedMessage = messageRepository.save(
                new Message(content, roomId, sender.getName(), sender.getId()));

        MessageDto response = messageMapper.messageTomessageDto(savedMessage);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, response);

        return response;
    }

    public MessagePageDto getMessages(User user, String roomId, String before, String after, Integer limit) {
        if (!roomService.isUserPresentInRoom(user.getId(), roomId)) {
            throw new AccessDeniedException("You are not a member of this room");
        }

        int pageSize = limit == null ? DEFAULT_PAGE_SIZE : Math.min(Math.max(limit, 1), MAX_PAGE_SIZE);

        KeysetPage<Message> page = mongoTemplateService.findByIdCursor(
                "roomId", roomId, before, after, pageSize, Message.class);

        return new MessagePageDto(
                page.items().stream().map(messageMapper::messageTomessageDto).toList(),
                page.hasMore());
    }
}
