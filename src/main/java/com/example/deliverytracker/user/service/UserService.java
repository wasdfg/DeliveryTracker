package com.example.deliverytracker.user.service;

import com.example.deliverytracker.global.jwt.JwtProvider;
import com.example.deliverytracker.user.repository.UserRepository;
import com.example.deliverytracker.user.dto.UserLoginRequest;
import com.example.deliverytracker.user.dto.UserSignupRequest;
import com.example.deliverytracker.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public void signup(UserSignupRequest request){
        String email = request.getEmail();
        String idForLogin = request.getIdForLogin();
        String nickname = request.getNickname();
        String phone = request.getPhone();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByIdForLogin(request.getIdForLogin())) {
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
                    .role(User.Role.USER)
                    .build();

            userRepository.save(user);
        }
    }

    public String login(UserLoginRequest request){

        User user = userRepository.findByIdForLogin(request.getIdForLogin())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.createToken(user.getId(), user.getRole().name());
    }
}
