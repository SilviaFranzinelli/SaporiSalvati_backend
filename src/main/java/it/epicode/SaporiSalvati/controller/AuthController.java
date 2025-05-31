package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.JwtUtil;
import it.epicode.SaporiSalvati.model.User;
import it.epicode.SaporiSalvati.repository.UserRepository;
import it.epicode.SaporiSalvati.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = passwordEncoder.encode(user.get("password"));
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username già esistente");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return ResponseEntity.ok("Registrazione completata");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token, "username", username));
    }

    @GetMapping("/users/me")
    public ResponseEntity<User> getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = UserService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}