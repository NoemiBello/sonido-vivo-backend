package cl.sonidovivo.authservice.config;

import cl.sonidovivo.authservice.model.Usuario;
import cl.sonidovivo.authservice.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

// Cuentas de prueba (las mismas del frontend). Contraseña guardada como hash.
// Cambiar antes de un despliegue público.
@Configuration
public class DatosIniciales {
  @Bean CommandLineRunner usuariosIniciales(UsuarioRepository repo,
      PasswordEncoder encoder) {
    return args -> {
      crear(repo, encoder, "admin@sonidovivo.cl", "ADMIN");
      crear(repo, encoder, "vendedor@sonidovivo.cl", "VENDEDOR");
      crear(repo, encoder, "camila@sonidovivo.cl", "USER");
    };
  }

  private void crear(UsuarioRepository repo, PasswordEncoder encoder,
      String correo, String rol) {
    if (repo.findByCorreoIgnoreCase(correo).isEmpty()) {
      Usuario u = new Usuario();
      u.setCorreo(correo);
      u.setPassword(encoder.encode("123456"));
      u.setRol(rol);
      u.setActivo(true);
      repo.save(u);
    }
  }
}
