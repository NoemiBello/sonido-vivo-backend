package cl.sonidovivo.usuariosservice.config;

import cl.sonidovivo.usuariosservice.model.Usuario;
import cl.sonidovivo.usuariosservice.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

// Datos personales de las mismas 3 cuentas de prueba creadas en authservice.
@Configuration
public class DatosIniciales {
  @Bean CommandLineRunner usuariosIniciales(UsuarioRepository repo) {
    return args -> {
      crear(repo, "admin@sonidovivo.cl", "Noemí", "Bello");
      crear(repo, "vendedor@sonidovivo.cl", "Diego", "Muñoz");
      crear(repo, "camila@sonidovivo.cl", "Camila", "Soto");
    };
  }

  private void crear(UsuarioRepository repo, String correo, String nombre, String apellido) {
    if (repo.findByCorreoIgnoreCase(correo).isEmpty()) {
      repo.save(new Usuario(null, correo, nombre, apellido, null, null));
    }
  }
}
