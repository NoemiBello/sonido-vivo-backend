package cl.sonidovivo.authservice.service;

import cl.sonidovivo.authservice.dto.*;
import cl.sonidovivo.authservice.model.Usuario;
import cl.sonidovivo.authservice.repository.UsuarioRepository;
import cl.sonidovivo.authservice.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UsuarioRepository usuarios;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(UsuarioRepository usuarios,
      PasswordEncoder encoder, JwtService jwt) {
    this.usuarios = usuarios;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  public LoginResponse login(LoginRequest request) {
    Usuario usuario = usuarios.findByCorreoIgnoreCase(request.correo())
      .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
    // Mismo mensaje para clave incorrecta o cuenta desactivada:
    // no se revela qué dato falló.
    if (!usuario.isActivo() || !encoder.matches(request.password(), usuario.getPassword())) {
      throw new BadCredentialsException("Credenciales inválidas");
    }
    return new LoginResponse(jwt.generar(usuario),
      usuario.getCorreo(), usuario.getRol());
  }
}
