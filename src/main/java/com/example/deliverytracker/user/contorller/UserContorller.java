package com.example.deliverytracker.user.contorller;

import com.example.deliverytracker.global.jwt.JwtProvider;
import com.example.deliverytracker.review.dto.ReviewResponse;
import com.example.deliverytracker.user.dto.PasswordCheckRequest;
import com.example.deliverytracker.user.dto.LoginResponse;
import com.example.deliverytracker.user.dto.ResetPasswordRequest;
import com.example.deliverytracker.user.dto.UserEmailRequest;
import com.example.deliverytracker.user.dto.UserInfoRequest;
import com.example.deliverytracker.user.dto.UserLoginRequest;
import com.example.deliverytracker.user.dto.UserPasswordRequest;
import com.example.deliverytracker.user.dto.UserSignupRequest;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import com.example.deliverytracker.user.dto.UserResponse;
import com.example.deliverytracker.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserContorller {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> signup(@RequestPart("request") @Valid UserSignupRequest request,@RequestPart("image") MultipartFile imageFile){

        this.userService.signup(request,imageFile);
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        String token = jwtProvider.resolveToken(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        long expirationMillis = jwtProvider.getRemainingTime(token);

        userService.logout(token, expirationMillis);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserResponse response = userService.getMyInfo(userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/check")
    public ResponseEntity<String> checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid PasswordCheckRequest request){

        User user = userDetails.getUser();

        this.userService.checkPassword(user,request.getPassword());

        return ResponseEntity.ok("비밀번호가 일치합니다.");
    }

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("request") @Valid UserInfoRequest request,@RequestPart("image") MultipartFile imageFile){

        this.userService.updateMyInfo(userDetails.getUser(),request,imageFile);

        return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserPasswordRequest request){

        User user = userDetails.getUser();

        this.userService.changePassword(user,request);

        return ResponseEntity.ok("비밀번호가 성공적으로 수정되었습니다. 다시 로그인 해주세요.");
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid PasswordCheckRequest request) {

        this.userService.deleteUser(userDetails.getUser(), request);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    @PostMapping("/findIdForLogin")
    public ResponseEntity<String> sendIdForLogin(@RequestBody @Valid UserEmailRequest request){
        this.userService.sendIdForLogin(request);

        return ResponseEntity.ok("가입된 이메일로 아이디를 전송했습니다.");
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> sendPasswordMail(@RequestBody @Valid UserEmailRequest request) {
        userService.sendPasswordEmail(request);

        return ResponseEntity.ok("가입된 이메일로 비밀번호를 전송했습니다.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/reviews")
    public ResponseEntity<Page<ReviewResponse>> viewMyReview(@AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable){

        Page<ReviewResponse> review = this.userService.viewMyReview(userDetails.getUser(),pageable);

        return ResponseEntity.ok(review);
    }
}
