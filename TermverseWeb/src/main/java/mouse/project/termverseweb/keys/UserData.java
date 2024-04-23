package mouse.project.termverseweb.keys;

import lombok.Data;

@Data
public class UserData {
    private String username;
    private String password;
    private Long dbId;
    private String email;
}
