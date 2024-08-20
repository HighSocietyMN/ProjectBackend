package com.royal.backend.user.controller;

import com.royal.backend.user.dto.UserDTO;
import com.royal.backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        try {
            userService.signup(userDTO);
            return ResponseEntity.ok("# 회원가입 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        if (userService.login(userDTO)) {
            return ResponseEntity.ok("# 로그인 성공");
        } else {
            return ResponseEntity.badRequest().body("# 유효하지 않은 인증정보");
        }
    }
}
