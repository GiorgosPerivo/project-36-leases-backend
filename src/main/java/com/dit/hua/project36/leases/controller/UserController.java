package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.entity.ERole;
import com.dit.hua.project36.leases.entity.User;
import com.dit.hua.project36.leases.payload.request.CreateUserRequest;
import com.dit.hua.project36.leases.payload.response.MessageResponse;
import com.dit.hua.project36.leases.repository.RoleRepository;
import com.dit.hua.project36.leases.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                encoder.encode(createUserRequest.getPassword()));

        String roleString = createUserRequest.getRole();
        user.setRole(roleRepository.findByName(roleString)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<?> removeUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        userRepository.delete(user.get());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User removed successfully!"));
    }
}
