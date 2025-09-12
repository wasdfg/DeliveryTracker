package com.example.deliverytracker.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @Pattern(regexp = "^(010|011)-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    private String address;

    private String imageUrl;

    private Boolean deleteImage;
}
