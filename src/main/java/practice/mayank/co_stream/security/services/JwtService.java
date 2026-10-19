package practice.mayank.co_stream.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import practice.mayank.co_stream.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


@Service
public class JwtService {

    private final SecretKey secretKey;

    private final Long jwtExpiryTime;

    public JwtService(
            @Value("${security.jwt.secret_key}")
            String secretKey,

            @Value("${security.jwt.expiry_time}")
            Long jwtExpiryTime
    ) {
        byte[] keyBytes = secretKey.getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpiryTime = jwtExpiryTime;
    }

    public String generateToken(User user) {
        return Jwts
                .builder()
                .header()
                .type("Access Token")
                .and()
                .subject(user.getEmail())
                .claim("role"   , user.getRole().getRoleName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiryTime))
                .signWith(secretKey)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean checkExpiration(String token) {
        return extractExpiration(token).after(new Date());
    }

    // Expiration time and Token Check
    public boolean isTokenValid(String token) {
        return checkExpiration(token);
    }


}
