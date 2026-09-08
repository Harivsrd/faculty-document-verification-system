package com.example.faculty.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private UserResponse user;
=======
public record LoginResponse(
        String token,
        String tokenType,
        UserResponse user
) {
    public static LoginResponse of(String token, UserResponse user) {
        return new LoginResponse(token, "Bearer", user);
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
