package com.web.blog.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.ColumnDefault;

@Valid
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String useremail;
    @ApiModelProperty(required = true)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    String password;
    @ApiModelProperty(required = true)
    @NotNull
    String username;
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    String phonenumber;
    String profileimg;
    String introduction;
    int age;
    String gender;
    String isadmin;
    String issocial;

}
