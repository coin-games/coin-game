package com.cgs.backend;

import com.cgs.backend.dto.UserSignUpRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import com.cgs.backend.service.user.UserSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserSignUpService userSignUpService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody @Valid UserSignUpRequest request) {
        userSignUpService.signUp(request);
        return ResponseEntity.ok().build();
    }
}
