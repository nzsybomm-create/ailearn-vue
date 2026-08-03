package com.ailearn.security;

import com.ailearn.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (!jwtUtil.isExpired(token)) {
                    Long userId = jwtUtil.getUserId(token);
                    String role = jwtUtil.getRole(token);
                    User user = new User();
                    user.setId(userId);
                    user.setRole(com.ailearn.entity.enums.Role.valueOf(role));
                    LoginUser loginUser = new LoginUser(userId, null, null,
                            com.ailearn.entity.enums.Role.valueOf(role));
                    loginUser.setUserId(userId);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            loginUser, null, loginUser.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {
                // invalid token -> anonymous
            }
        }
        filterChain.doFilter(request, response);
    }
}
