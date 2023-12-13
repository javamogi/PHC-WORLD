package com.phcworld.api.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UserRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 4, message = "4자 이상으로 해야합니다.")
    @JsonIgnore
    private String password;

    @NotNull
    @Size(min = 3, max = 20, message = "영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "한글, 영문, 숫자만 가능합니다.")
    private String name;
}
