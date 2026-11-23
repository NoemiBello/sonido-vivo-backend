package cl.sonidovivo.authservice.service;

import cl.sonidovivo.authservice.dto.*;
import cl.sonidovivo.authservice.exception.DatosInvalidosException;
import cl.sonidovivo.authservice.model.Usuario;
import cl.sonidovivo.authservice.repository.UsuarioRepository;
import cl.sonidovivo.authservice.security.JwtService;
import io.jsonwebtoken.Claims;
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

  // Registro público desde la tienda: siempre crea un cliente (USER)
  // y devuelve el token para que React quede con la sesión iniciada.
  public LoginResponse registrar(RegistroRequest request) {
    if (usuarios.findByCorreoIgnoreCase(request.correo()).isPresent()) {
      throw new DatosInvalidosException("El correo ya está registrado");
    }
    if (request.password().length() < 6) {
      throw new DatosInvalidosException("La contraseña debe tener al menos 6 caracteres");
    }
    Usuario usuario = new Usuario();
    usuario.setCorreo(request.correo().trim().toLowerCase());
    usuario.setPassword(encoder.encode(request.password()));
    usuario.setRol("USER");
    usuario.setActivo(true);
    usuarios.save(usuario);
    return new LoginResponse(jwt.generar(usuario),
      usuario.getCorreo(), usuario.getRol());
  }

  // Renovación: el token recibido debe ser válido (firma, emisor y expiración).
  // Si lo es, y la cuenta sigue activa, se emite un token nuevo con el rol actual.
  public LoginResponse renovar(RefreshRequest request) {
    Claims claims = jwt.validar(request.token());
    Usuario usuario = usuarios.findByCorreoIgnoreCase(claims.getSubject())
      .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
    if (!usuario.isActivo()) {
      throw new BadCredentialsException("Credenciales inválidas");
    }
    return new LoginResponse(jwt.generar(usuario),
      usuario.getCorreo(), usuario.getRol());
  }
}
