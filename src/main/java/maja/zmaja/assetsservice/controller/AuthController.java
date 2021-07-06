package maja.zmaja.assetsservice.controller;


import maja.zmaja.assetsservice.dao.UserRolesRepository;
import maja.zmaja.assetsservice.entity.Role;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserRoles;
import maja.zmaja.assetsservice.enums.UserRole;
import maja.zmaja.assetsservice.entity.response.JwtResponse;
import maja.zmaja.assetsservice.entity.request.LoginRequest;
import maja.zmaja.assetsservice.entity.request.SignupRequest;
import  maja.zmaja.assetsservice.security.service.*;
import maja.zmaja.assetsservice.dao.RoleRepository;
import maja.zmaja.assetsservice.dao.UserRepository;
import maja.zmaja.assetsservice.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     *
     *Authentication user by username & password; create and return JWT token
     *
     **/
    @PostMapping (value = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
       try {
           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           String jwt = jwtUtils.generateJwtToken(authentication);
           UserDetailsSecurity userDetails = (UserDetailsSecurity) authentication.getPrincipal();
           List<String> roles = userDetails.getAuthorities().stream()
                   .map(item -> item.getAuthority())
                   .collect(Collectors.toList());

           return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(new JwtResponse(jwt, loginRequest.getUsername(), roles));
       }catch (Exception e) {
           return ResponseEntity.badRequest().body("Error " + e.getMessage());
       }
       }
    /**
     *
     * User with role Admin can create other user
     *
     **/
    @PreAuthorize("hasAuthority('ADMIN')")    
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body("Error: Username exist!");
            }
            User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
            Set<String> signUpRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();
            if (signUpRoles == null) {
                Role userRole = roleRepository.findByRole(UserRole.USER);
                if (userRole == null)
                    throw new RuntimeException("Error: Role is not found.");
                roles.add(userRole);
            } else {
                for (String role : signUpRoles) {
                    if (role.toLowerCase(Locale.ROOT).equals("admin")) {
                        Role adminRole = roleRepository.findByRole(UserRole.ADMIN);
                        if (adminRole == null)
                            throw new RuntimeException("Error: Role is not found.");
                        roles.add(adminRole);
                    }
                    if (role.toLowerCase(Locale.ROOT).equals("user")) {
                        Role userRole = roleRepository.findByRole(UserRole.USER);
                        if (userRole == null)
                            throw new RuntimeException("Error: Role is not found.");
                        roles.add(userRole);
                    }
                }
            }
            userRepository.save(user);
            for (Role role : roles) {
                UserRoles userRoles = new UserRoles(role, user);
                userRolesRepository.save(userRoles);
            }

            return ResponseEntity.ok("User registered successfully!");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

}
