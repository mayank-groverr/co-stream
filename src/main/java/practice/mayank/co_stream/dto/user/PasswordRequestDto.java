package practice.mayank.co_stream.dto.user;

public record PasswordRequestDto(
        String oldPassword,
        String newPassword
) {
}
