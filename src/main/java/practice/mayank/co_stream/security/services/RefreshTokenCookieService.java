package practice.mayank.co_stream.security.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import practice.mayank.co_stream.exception.customexceptions.BadRequestException;

import java.time.Duration;

@Service
public class RefreshTokenCookieService {

    private final String cookieName;
    private final String cookieDomain;
    private final String cookieSameSite;

    public RefreshTokenCookieService(
            @Value("${security.refresh_token.cookie_name}") String cookieName,
            @Value("${security.refresh_token.cookie_domain}") String cookieDomain,
            @Value("${security.refresh_token.cookie_same_site}") String cookieSameSite
            ) {
        this.cookieName = cookieName;
        this.cookieDomain = cookieDomain;
        this.cookieSameSite = cookieSameSite;
    }


    public void attachRefreshTokenToCookie(HttpServletResponse response, String refreshToken, Long maxAge){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("*")
                .maxAge(Duration.ofMinutes(maxAge))
                .sameSite(cookieSameSite);

        if(cookieDomain != null && !cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie responseCookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }


    public void clearRefreshCookie(HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(cookieName, "").
                httpOnly(true).
                secure(false).
                maxAge(0)
                .path("*").
                sameSite(cookieSameSite);

        if(cookieDomain != null && !cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }
        ResponseCookie responseCookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public String readRefreshTokenFromRequest(HttpServletRequest request) {
        if(request.getCookies() != null){
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        throw new BadRequestException("Refresh token Missing");
    }
}
