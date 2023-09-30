package com.phcworld.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.exception.model.CustomBaseException;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorMessage = objectMapper.writeValueAsString(e.getMessages());
            response.getWriter().write(errorMessage);
        } catch (TokenException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorMessage = objectMapper.writeValueAsString(e.getTokenDto());
            response.getWriter().write(errorMessage);
        } catch (CustomBaseException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorMessage = objectMapper.writeValueAsString(e.getErrorCode().getMessage());
            response.getWriter().write(errorMessage);
        }
    }
}
