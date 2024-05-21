package mouse.project.termverseweb.filters.spring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OptionalAuthenticationFilter extends OncePerRequestFilter {
    private final JwtDecoder decoder;
    public OptionalAuthenticationFilter(JwtDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        OptionalAuthentication auth;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String subject = decoder.decode(token).getSubject();
            auth = OptionalAuthentication.of(subject);
        } else {
            auth = OptionalAuthentication.empty();
        }
        SecurityContextHolder.getContext().setAuthentication(new OptionalAuthenticationToken(auth));
        filterChain.doFilter(request, response);
    }
}
