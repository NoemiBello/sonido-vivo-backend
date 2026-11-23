package cl.sonidovivo.authservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Credenciales de acceso. Los datos personales (nombre, teléfono, etc.)
// viven en usuariosservice; aquí solo lo necesario para emitir el JWT.
@Entity @Table(name = "usuarios")
@Getter @Setter
public class Usuario {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String correo;

  // Hash BCrypt, nunca la contraseña legible
  @Column(nullable = false)
  private String password;

  // ADMIN, VENDEDOR o USER
  @Column(nullable = false)
  private String rol;

  // Un usuario desactivado no puede iniciar sesión
  @Column(nullable = false)
  private boolean activo = true;
}
