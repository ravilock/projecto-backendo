package com.example.mapaCife.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

import com.example.mapaCife.dto.RegisterDTO;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<User> register(@RequestBody RegisterDTO registerDTO) {

        if (userRepository.findByUsername(registerDTO.username()) != null) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUsername(registerDTO.username());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.password()));
        user.setRole(registerDTO.role());
        user.setName(registerDTO.name());
        user.setEmail(registerDTO.email());
        user.setCreatedAt(new Date());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

}
