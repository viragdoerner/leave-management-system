package lms.backend.auth.api;

import lms.backend.auth.JwtProvider;
import lms.backend.auth.JwtResponse;
import lms.backend.auth.dto.LoginForm;
import lms.backend.auth.dto.RegisterForm;
import lms.backend.auth.dao.RoleRepository;
import lms.backend.auth.dto.UserDTO;
import lms.backend.auth.model.Role;
import lms.backend.auth.model.RoleName;
import lms.backend.dao.UserRepository;
import lms.backend.exception.CustomMessageException;
import lms.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
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
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/setup/roles")
    public ResponseEntity<?> setupRoles() {

        //hozzáadom a használt szerepeket
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        roleRepository.save(new Role(RoleName.ROLE_USER));

        return new ResponseEntity<>("Admin and user roles successfully added!",
                HttpStatus.OK);
    }
    @PostMapping("/ping")
    public ResponseEntity<?> echo() {

        return new ResponseEntity<>("Pong!",
                HttpStatus.OK);
    }
    @PostMapping("/setup/admin")
    public ResponseEntity<?> setupAdmin() {

        //leellenőrzöm hogy létre lett-e már hozva ilyen felhasználó
        if (userRepository.existsByEmail("boss@boss.com")) {
            return new ResponseEntity<>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        //hozzáadom az admint
        User user = new User("The Boss", "boss@boss.com","boss@boss.com",
                encoder.encode("password"), 0, false);

        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(adminRole);
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>("Admin user succesfully added!",
                HttpStatus.OK);
    }
    @PostMapping("/setup/user")
    public ResponseEntity<?> setupUser() {

        //leellenőrzöm hogy létre lett-e már hozva ilyen felhasználó
        if (userRepository.existsByEmail("user@user.com")) {
            return new ResponseEntity<>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        //hozzáadom az admint
        User user = new User("A User", "user@user.com","user@user.com",
                encoder.encode("password"), 50, false);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>("Simple user succesfully added!",
                HttpStatus.OK);
    }
}

