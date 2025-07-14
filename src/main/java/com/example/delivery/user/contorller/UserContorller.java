package com.example.delivery.user.contorller;

import com.example.delivery.user.dto.LoginResponse;
import com.example.delivery.user.dto.UserLoginRequest;
import com.example.delivery.user.dto.UserSignupRequest;
import com.example.delivery.user.entitiy.User;
import com.example.delivery.user.entitiy.UserDetailsImpl;
import com.example.delivery.user.entitiy.UserResponse;
import com.example.delivery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserContorller {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequest request){

        this.userService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest request){
        String token = userService.login(request);
        LoginResponse response = new LoginResponse(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        return ResponseEntity.ok(new UserResponse(user));
    }

}
