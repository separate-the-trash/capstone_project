package com.example.demo.config.jwt;

import com.example.demo.config.auth.UserAccount;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.config.jwt.JwtProperties.*;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtProcessor jwtProcessor;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProcessor jwtProcessor) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProcessor = jwtProcessor;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = request.getHeader(HEADER_STRING);

        // header가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtProcessor.extractBearer(jwtHeader);
        String username = jwtProcessor.decodeJwtToken(jwtToken, SECRET, "username");

        if (username != null){
            log.info("username 정상");
            log.info("user : " + username);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));

            UserAccount userAccount = new UserAccount(user);
            log.info("principalDetails : " + userAccount.getUsername() + "가 잘들어왔습니다.");

            Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
