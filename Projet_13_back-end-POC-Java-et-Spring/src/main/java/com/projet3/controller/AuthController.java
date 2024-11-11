package com.projet3.controller;

import com.projet3.dto.UserDTO;
import com.projet3.dto.UserLoginDTO;
import com.projet3.dto.UserRegisterDTO;
import com.projet3.model.AuthSuccess;
import com.projet3.repository.UserRepository;
import com.projet3.service.AuthService;
import com.projet3.service.UserDetailsServiceImpl;
import com.projet3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,UserService userService ) {
        this.authService = authService;
        this.userService = userService;

    }
    @PostMapping("/register")
    public ResponseEntity<AuthSuccess> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            AuthSuccess authSuccess = authService.register(userRegisterDTO);
            return ResponseEntity.ok(authSuccess);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(null); // or return a custom error response
        } catch (DataIntegrityViolationException e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(409).body(null); // conflict, e.g., email already in use
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // internal server error
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccess> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            AuthSuccess authSuccess = authService.login(userLoginDTO);
            return ResponseEntity.ok(authSuccess);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthSuccess("Invalid email or password"));
        }
    }
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String currentPrincipalName = authentication.getName();
        return ResponseEntity.ok(userService.getUserByEmail(currentPrincipalName));
    }
}
