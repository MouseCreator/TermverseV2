package mouse.project.termverseweb.security;

public class TokenServiceMock implements TokenService {
    @Override
    public String getSubject(String token) {
        return token;
    }
}
