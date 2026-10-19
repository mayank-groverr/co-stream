package practice.mayank.co_stream.security.dto;

import java.util.Date;

public record SecurityTokenResponse(
        String jwt,
        Date expiry
) {
}
