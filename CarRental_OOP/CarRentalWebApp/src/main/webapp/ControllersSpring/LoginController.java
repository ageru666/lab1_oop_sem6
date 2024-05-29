package main.webapp.ControllersSpring;

import main.webapp.Data.Models.Client;
import main.webapp.Data.Models.Login;
import main.webapp.ServicesSpring.LoginService;
import main.webapp.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginController(LoginService loginService, JwtUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (login.getUsername() == null || login.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Missing parameters\"}");
        }

        try {
            Client foundClient = loginService.findByUsername(login.getUsername());
            if (foundClient == null) {
                return ResponseEntity.status(404).body("{\"error\": \"User not found\"}");
            }

            if (BCrypt.checkpw(login.getPassword(), foundClient.getPassword())) {
                String token = jwtUtil.generateToken(login.getUsername(), foundClient.getRole());
                return ResponseEntity.ok("{\"jwt\": \"" + token + "\", \"clientId\": " + foundClient.getId() + "}");
            } else {
                return ResponseEntity.status(401).body("{\"error\": \"Invalid password\"}");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }
}
