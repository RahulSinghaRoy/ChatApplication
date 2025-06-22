//package com.jwt;
//
//import java.util.Collections;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//	@Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserEntity user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return ResponseEntity.ok(userRepo.save(user));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserEntity user) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        String token = jwtUtil.generateToken(user.getUsername());
//        return ResponseEntity.ok(Collections.singletonMap("token", token));
//    }
//}
