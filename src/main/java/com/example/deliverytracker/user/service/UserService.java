package com.example.deliverytracker.user.service;

import com.example.deliverytracker.global.jwt.JwtProvider;
import com.example.deliverytracker.mail.service.EmailService;
import com.example.deliverytracker.user.dto.PasswordCheckRequest;
import com.example.deliverytracker.user.dto.UserEmailRequest;
import com.example.deliverytracker.user.dto.UserInfoRequest;
import com.example.deliverytracker.user.dto.UserPasswordRequest;
import com.example.deliverytracker.user.entitiy.EmailType;
import com.example.deliverytracker.user.repository.UserRepository;
import com.example.deliverytracker.user.dto.UserLoginRequest;
import com.example.deliverytracker.user.dto.UserSignupRequest;
import com.example.deliverytracker.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final StringRedisTemplate redisTemplate;

    private final EmailService emailService;

    private static final Duration TOKEN_EXPIRATION = Duration.ofMinutes(30);


    public void signup(UserSignupRequest request){
        String email = request.getEmail();
        String idForLogin = request.getIdForLogin();
        String nickname = request.getNickname();
        String phone = request.getPhone();
        String address = request.getAddress();

        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if(userRepository.existsByIdForLogin(request.getIdForLogin())){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        else{
            String encryptedPassword = passwordEncoder.encode(request.getPassword());

            User user = User.builder()
                    .email(email)
                    .idForLogin(idForLogin)
                    .password(encryptedPassword)
                    .nickname(nickname)
                    .phone(phone)
                    .createdAt(LocalDateTime.now())
                    .address(address)
                    .role(User.Role.USER)
                    .status(User.Status.ACTIVE)
                    .build();

            userRepository.save(user);
        }
    }

    public String login(UserLoginRequest request){

        User user = userRepository.findByIdForLogin(request.getIdForLogin())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if(user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getStatus() == User.Status.WITHDRAWN) {
            long days = Duration.between(user.getModifiedAt(), LocalDateTime.now()).toDays();

            if(days <= 30){
                throw new IllegalArgumentException("탈퇴한 계정입니다. 복구하시겠습니까?");
            }
            else{
                throw new IllegalArgumentException("계정이 삭제되었습니다.");
            }

        }

        if (user.getStatus() == User.Status.SUSPENDED) {
            throw new IllegalArgumentException("정지된 계정입니다. 관리자에게 문의하세요.");
        }

        user.changeDate(LocalDateTime.now());

        return jwtProvider.createToken(user.getId(), user.getRole().name());
    }

    public void logout(String token, long expirationMillis) {
        String redisKey = "blacklist:" + token;
        redisTemplate.opsForValue().set(redisKey, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public void checkPassword(User user,String inputPassword){
        String realPassword = user.getPassword();

        if(!passwordEncoder.matches(inputPassword, realPassword)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

    }

    @Transactional
    public void changeInfo(User user, UserInfoRequest request){

        if (isBlankOrNull(request.getNickname())) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }

        user.changeInfo(
                request.getEmail(),
                request.getNickname(),
                request.getPhone(),
                request.getAddress()
        );
    }

    private boolean isBlankOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Transactional
    public void changePassword(User user, UserPasswordRequest request){

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if(!request.getToChangePassword1().equals(request.getToChangePassword2())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(request.getToChangePassword1(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");
        }

        String encryptedPassword = passwordEncoder.encode(request.getToChangePassword1());


        user.changePassword(encryptedPassword);

    }

    @Transactional
    public void deleteUser(User user, PasswordCheckRequest request) {
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.changeStatus(User.Status.WITHDRAWN);
        user.changeDate(LocalDateTime.now());
    }

    public void sendIdForLogin(UserEmailRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        this.sendEmail(user, EmailType.FIND_ID);
    }

    public void sendPasswordEmail(UserEmailRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        this.sendEmail(user, EmailType.FIND_PASSWORD);
    }

    public void sendEmail(User user,EmailType type){
        String subject;
        String body;


        switch (type) {
            case FIND_ID -> {
                subject = "요청하신 로그인 아이디입니다.";
                body = "회원님의 아이디는 " + user.getIdForLogin() + " 입니다.";
            }
            case FIND_PASSWORD -> {
                subject = "비밀번호 재설정 링크입니다.";

                String token = UUID.randomUUID().toString();

                String redisKey = "reset-password:" + token;

                redisTemplate.opsForValue().set(redisKey, user.getEmail(), TOKEN_EXPIRATION);

                String resetUrl = "https://yourdomain.com/reset-password?token=" + token;

                body = """
                        비밀번호를 재설정하려면 아래 링크를 클릭해주세요:

                        %s

                        본 링크는 30분간 유효합니다.
                        """.formatted(resetUrl);
            }
            default -> throw new IllegalArgumentException("잘못된 이메일 타입입니다.");
        }

        emailService.send(user.getEmail(), subject, body);
    }

    public void resetPassword(String token, String newPassword) {
        String redisKey = "reset-password:" + token;
        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            throw new IllegalArgumentException("토큰이 유효하지 않거나 만료되었습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        user.changePassword(passwordEncoder.encode(newPassword));
        user.changeDate(LocalDateTime.now());
        redisTemplate.delete(redisKey);
    }
}
