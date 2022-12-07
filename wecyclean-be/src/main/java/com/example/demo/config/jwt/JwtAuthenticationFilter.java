package com.example.demo.config.jwt;


import com.example.demo.config.auth.UserAccount;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
// jwt 인증 클래스, UsernamePasswordAuthenticationFilter 가 로그인시 인증을 담당
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProcessor jwtProcessor;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("JwtAuthenticationFilter : 로그인 시도중");
        ObjectMapper objectMapper = new ObjectMapper();
        User User = objectMapper.readValue(request.getInputStream(), User.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(User.getUsername(), User.getPassword());

        // DB에 잇는 username과 password가 일치하면 token을 던저 준다.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("로그인이 완료되었습니다. username = {}" + authentication);
        // 리턴의 이유는 권한 관리를 security가 대신 해주기 떄문에 편하려고 하는 거다.
        return authentication;
    }

    // 인증이 완료되면 jwtProcessor에서 token을 만들어서 던저준다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 실행됨 : 인증이 완료되었습니다.");
        UserAccount userAccount = (UserAccount) authResult.getPrincipal();
        String jwtToken = jwtProcessor.createJwtToken(userAccount);
        // 로그인 인증 성공 시 header 부분에 Token 생성
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + " " + jwtToken);
    }
}