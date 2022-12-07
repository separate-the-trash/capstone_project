package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    // login 조회
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("account/login");
        return modelAndView;
    }

    // register 조회
    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("account/register");
        return modelAndView;
    }


    // localhost8080/account/login 했을 시 header에 토큰 값 넘겨주기
    @PostMapping("/login")
    @ResponseBody
//    public String login(@RequestBody Map<String, String> users){
    public User login(@RequestBody Map<String, String> users){
        User user = userRepository.findByUsername(users.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 username입니다."));
        if (!passwordEncoder.matches(users.get("password"), user.getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호 입니다.");
        }
//        return user.toString() + "\n 로그인이 되었습니다.";
        return user;
    }

    // Json 반환 성공
    @PostMapping("/register")
    @ResponseBody
    public User register(@RequestBody User user){
//        user.setId(user.getId());
//        user.setUsername(user.getUsername());
        log.info("회원가입이 완료되었습니다.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setPoint(1000);
        userRepository.save(user);
        return user;
    }

    // user 권한을 가진 사용자가 들어갈 수 있은 테스트용
    @GetMapping("/api/user")
    @ResponseBody
    public String user(){
        // user 확인용
        return "user 권한이 있습니다.";
    }

    // 정보가 잘 저장되었는지 확인하는 테스트용
    @GetMapping("/users")
    public List<User> users() {
        return userRepository.findAll();
    }
}

