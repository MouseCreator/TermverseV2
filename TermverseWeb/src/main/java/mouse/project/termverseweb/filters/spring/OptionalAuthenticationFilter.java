package mouse.project.termverseweb.filters.spring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import mouse.project.termverseweb.filters.JWTHelper;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OptionalAuthenticationFilter extends OncePerRequestFilter {
    private final JwtDecoder decoder;
    private final JWTHelper helper;
    public OptionalAuthenticationFilter(JwtDecoder decoder, JWTHelper helper) {
        this.decoder = decoder;
        this.helper = helper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = fromRequest(request);
        OptionalAuthentication auth;
        if (token != null) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String subject = decoder.decode(token).getSubject();
            auth = OptionalAuthentication.of(subject);
        } else {
            auth = OptionalAuthentication.empty();
        }
        SecurityContextHolder.getContext().setAuthentication(new OptionalAuthenticationToken(auth));
        filterChain.doFilter(request, response);
    }

    private String fromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return helper.getAuthToken(cookies);
    }
}
