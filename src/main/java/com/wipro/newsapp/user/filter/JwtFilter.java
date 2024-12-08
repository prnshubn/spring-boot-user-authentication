package com.wipro.newsapp.user.filter;

import com.wipro.newsapp.user.model.User;
import com.wipro.newsapp.user.repository.BlackListedTokenRepository;
import com.wipro.newsapp.user.repository.UserRepository;
import com.wipro.newsapp.user.util.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;
        User user = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            userEmail = jwtUtility.getEmailFromToken(token);
            user = userRepository.findByEmail(userEmail).orElseThrow();
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null
                && blackListedTokenRepository.findByToken(token).isEmpty()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtUtility.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }
        filterChain.doFilter(request, response);
    }
}
