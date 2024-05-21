package mouse.project.termverseweb.security;

public interface TokenService {
    String getSubject(String token);
}
