package com.example.deliverytracker.global.jwt;

import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JwtConstants.HEADER);

        if (header != null && header.startsWith(JwtConstants.PREFIX)) {

            String token = header.replace(JwtConstants.PREFIX, "");

            if (jwtProvider.validateToken(token)) {

                Long userId = jwtProvider.getUserId(token);

                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {

                    if (user.getStatus() == User.Status.SUSPENDED) {

                        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

                        if (user.getSuspendedUntil() == null) {

                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "정지된 회원입니다.");

                            return;
                        }

                        if (user.getSuspendedUntil().isAfter(now)) {

                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "정지된 회원입니다.");

                            return;
                        }
                    }

                    String role = jwtProvider.getRole(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}