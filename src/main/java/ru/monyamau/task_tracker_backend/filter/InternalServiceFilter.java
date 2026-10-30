package ru.monyamau.task_tracker_backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.monyamau.task_tracker_backend.exception.CustomAuthenticationException;

import java.io.IOException;
import java.util.List;

@Component
public class InternalServiceFilter extends OncePerRequestFilter {
    private static final String INTERNAL_SERVICE_AUTHORITY = "INTERNAL_SERVICE_AUTHENTICATION";

    private final String internalToken;
    private final String internalTokenHeader;
    private final HandlerExceptionResolver exceptionResolver;

    public InternalServiceFilter(@Value("${internal-service.token}") String internalToken, @Value("${internal-service.token-header}") String internalTokenHeader, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.internalToken = internalToken;
        this.internalTokenHeader = internalTokenHeader;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(internalTokenHeader);
        try {
            if (header == null || header.isBlank() || !header.equals(internalToken)) {
                SecurityContextHolder.clearContext();
                throw new CustomAuthenticationException("Не удалось аутентифицировать по токену");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken("Internal_Service", null, List.of(new SimpleGrantedAuthority(INTERNAL_SERVICE_AUTHORITY)));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return;
        }
        filterChain.doFilter(request, response);
    }
}