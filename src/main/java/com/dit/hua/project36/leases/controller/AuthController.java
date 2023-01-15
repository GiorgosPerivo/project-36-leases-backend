package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.config.JwtUtils;
import com.dit.hua.project36.leases.entity.ERole;
import com.dit.hua.project36.leases.entity.Role;
import com.dit.hua.project36.leases.entity.User;
import com.dit.hua.project36.leases.payload.request.LoginRequest;
import com.dit.hua.project36.leases.payload.request.SignupRequest;
import com.dit.hua.project36.leases.payload.response.JwtResponse;
import com.dit.hua.project36.leases.payload.response.MessageResponse;
import com.dit.hua.project36.leases.repository.RoleRepository;
import com.dit.hua.project36.leases.repository.UserRepository;
import com.dit.hua.project36.leases.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

       String roleString = signUpRequest.getRole();



        if (roleString == null) {
          user.setRole(roleRepository.findByName(ERole.ROLE_TENANT)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        } else {
            switch (roleString) {
                case "admin":
                    user.setRole(roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

                    break;
                case "leaser":
                    user.setRole(roleRepository.findByName(ERole.ROLE_LEASER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

                    break;
                default:
                    user.setRole(roleRepository.findByName(ERole.ROLE_TENANT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
            }

        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}