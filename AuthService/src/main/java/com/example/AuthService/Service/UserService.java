package com.example.AuthService.Service;

import com.example.AuthService.Model.JwtTokenResponse;
import com.example.AuthService.Model.User;
import com.example.AuthService.Model.UserDto;
import com.example.AuthService.Repos.UserRepository;
import com.example.AuthService.Util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRoles());

    }

    public JwtTokenResponse generateToken(String username) {
        String token = JwtUtil.generateToken(username);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setToken(token);
        jwtTokenResponse.setType("Bearer");
        jwtTokenResponse.setValidUntil(JwtUtil.extractExpiration(token).toString());
        return jwtTokenResponse;
    }
}
