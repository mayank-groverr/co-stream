package practice.mayank.co_stream.security.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.security.dto.SecurityTokenResponse;
import practice.mayank.co_stream.security.entity.RefreshToken;
import practice.mayank.co_stream.service.UserService;


@Service
@RequiredArgsConstructor
public class SecurityTokenService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookieService cookieService;
    private final UserService userService;


    @Transactional
    public SecurityTokenResponse generateSecurityToken(User user, HttpServletResponse response){
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        cookieService.attachRefreshTokenToCookie(response, refreshToken, refreshTokenService.getRefreshTokenExpiryTime());
        return new SecurityTokenResponse(accessToken, jwtService.extractExpiration(accessToken));
    }

    @Transactional
    public void logoutUser(HttpServletRequest request, HttpServletResponse response){
        String refreshTokenFromRequest = cookieService.readRefreshTokenFromRequest(request);
        refreshTokenService.invalidateRefreshToken(refreshTokenFromRequest);
        cookieService.clearRefreshCookie(response);
    }

    @Transactional
    public SecurityTokenResponse renewAccessTokenAndRefreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String refreshTokenFromRequest = cookieService.readRefreshTokenFromRequest(request);
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenFromRequest);
        String renewedRefreshToken = refreshTokenService.renewRefreshToken(refreshToken);
        String renewedJwtToken = jwtService.generateToken(refreshToken.getUser());
        cookieService.attachRefreshTokenToCookie(response, renewedRefreshToken, refreshTokenService.getRefreshTokenExpiryTime());
        return new SecurityTokenResponse(renewedJwtToken, jwtService.extractExpiration(renewedJwtToken));
    }

}
