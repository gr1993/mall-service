package com.park.mall.web.mall.auth.dto;

import com.park.mall.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegister {

    @NotBlank(message = "{NotBlank.member.name}")
    @Size(min = 2, max = 100, message = "{Size.member.name}")
    private String name;

    @NotBlank(message = "{NotBlank.member.email}")
    @Email(message = "{NotFormat.member.email}")
    private String email;

    @NotBlank(message = "{NotBlank.member.id}")
    @Size(min = 4, max = 100, message = "{Size.member.id}")
    private String id;

    @NotBlank(message = "{NotBlank.member.password}")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
            message = "{NotFormat.member.password}"
    )
    private String password;

    private String confirmPassword;

    public Member getMember() {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password);
        member.setName(this.name);
        member.setEmail(this.email);
        return member;
    }
}
