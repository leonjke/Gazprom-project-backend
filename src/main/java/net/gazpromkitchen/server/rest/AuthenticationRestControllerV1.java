package net.gazpromkitchen.server.rest;

import io.swagger.v3.oas.annotations.Operation;
import net.gazpromkitchen.server.dto.AuthenticationRequestDto;
import net.gazpromkitchen.server.dto.RegistrationRequestDto;
import net.gazpromkitchen.server.model.User;
import net.gazpromkitchen.server.security.jwt.JwtTokenProvider;
import net.gazpromkitchen.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Operation(summary = "Login from service")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Operation(summary = "Registration in service")
    @PostMapping("registration")
    public ResponseEntity register(@RequestBody RegistrationRequestDto requestDto) {
        Map<Object, Object> response = new HashMap<>();
        if (userService.findByUsername(requestDto.getUsername()) != null) {
            response.put("status", "User with that username already exists.");
            return ResponseEntity.ok(response);
        }
        User user = requestDto.toUser();
        user = userService.register(user);

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        response.put("username", user.getUsername());
        response.put("status", "ok");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


}
