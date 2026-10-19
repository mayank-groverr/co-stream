package practice.mayank.co_stream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.mayank.co_stream.dto.user.PasswordRequestDto;
import practice.mayank.co_stream.dto.user.UserResponse;
import practice.mayank.co_stream.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Relies on the security context to fetch the logged-in user
    @GetMapping("/get/me")
    public ResponseEntity<UserResponse> getUser() {
        UserResponse userResponse = userService.getUser();
        return ResponseEntity.ok(userResponse);
    }

    // Updates password for the currently logged-in user
    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequestDto passwordRequestDto) {
        String responseMessage = userService.updatePassword(passwordRequestDto);
        return ResponseEntity.ok(responseMessage);
    }

    // Deletes the currently logged-in user
    @DeleteMapping("/delete/me")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}