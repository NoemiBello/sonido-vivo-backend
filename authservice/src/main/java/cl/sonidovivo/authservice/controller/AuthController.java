package cl.sonidovivo.authservice.controller;

import cl.sonidovivo.authservice.dto.*;
import cl.sonidovivo.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService auth;
  public AuthController(AuthService auth) { this.auth = auth; }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return auth.login(request);
  }

  @PostMapping("/registro")
  public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody RegistroRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(auth.registrar(request));
  }

  @PostMapping("/refresh")
  public LoginResponse renovar(@Valid @RequestBody RefreshRequest request) {
    return auth.renovar(request);
  }
}
