package practice.mayank.co_stream.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.co_stream.dto.user.LoginRequest;
import practice.mayank.co_stream.dto.user.UserRequest;
import practice.mayank.co_stream.dto.user.UserResponse;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.security.dto.SecurityTokenResponse;
import practice.mayank.co_stream.security.services.SecurityTokenService;
import practice.mayank.co_stream.service.UserService;


@RestController
@RequestMapping("/public/v1")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final SecurityTokenService securityTokenService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @RequestBody UserRequest userRequest
    ) {
        UserResponse newUser = userService.createNewUser(userRequest);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Request with Credentials -> Verify -> Return token if valid
    @PostMapping("/login")
    public ResponseEntity<SecurityTokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User authenticate = userService.authenticate(loginRequest);
        SecurityTokenResponse securityTokenResponse = securityTokenService.generateSecurityToken(authenticate, response);
        return ResponseEntity.ok(securityTokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SecurityTokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityTokenResponse refreshTokenResponse =
                securityTokenService.renewAccessTokenAndRefreshToken(request, response);
        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        securityTokenService.logoutUser(request, response);
        return ResponseEntity.noContent().build();
    }
}
