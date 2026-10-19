package practice.mayank.co_stream.dto.message;

import java.util.List;

public record MessagePageDto(
        List<MessageDto> messages,
        boolean hasMore
) {
}
