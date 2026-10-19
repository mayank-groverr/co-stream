package practice.mayank.co_stream.dto.user;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
