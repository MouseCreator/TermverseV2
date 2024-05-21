package mouse.project.termverseweb.dto.register;

import lombok.Data;

@Data
public class UserTokensDTO {
    private Long id;
    private String accessToken;
    private String refreshToken;

    public UserTokensDTO(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
