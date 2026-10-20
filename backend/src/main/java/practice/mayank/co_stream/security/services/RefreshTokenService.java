package practice.mayank.co_stream.security.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.exception.customexceptions.ResourceNotFoundException;
import practice.mayank.co_stream.security.exceptions.TokenExpiredException;
import practice.mayank.co_stream.security.entity.RefreshToken;
import practice.mayank.co_stream.security.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {


    @Getter
    @Value("${security.refresh_token.expiry_time}")
    private Long refreshTokenExpiryTime;
    private final RefreshTokenRepository refreshTokenRepository;



    public String createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryTime(Instant.now().plus(Duration.ofMinutes(refreshTokenExpiryTime)));
        RefreshToken newToken = refreshTokenRepository.save(token);
        return newToken.getToken();
    }

    public String renewRefreshToken(RefreshToken refreshToken) {
        if (isTokenExpired(refreshToken)) {
            deleteRefreshToken(refreshToken);
            throw new TokenExpiredException("Token Expired");
        }
        deleteRefreshToken(refreshToken);
        return createRefreshToken(refreshToken.getUser());
    }

    @Transactional
    public void invalidateRefreshToken(String refreshTokenFromRequest) {
        RefreshToken refreshToken = findRefreshToken(refreshTokenFromRequest);
        deleteRefreshToken(refreshToken);
    }

    private boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryTime().isBefore(Instant.now());
    }

    public RefreshToken findRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.orElseThrow(() -> new ResourceNotFoundException("Invalid token: " + token));
    }

    private void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

}
