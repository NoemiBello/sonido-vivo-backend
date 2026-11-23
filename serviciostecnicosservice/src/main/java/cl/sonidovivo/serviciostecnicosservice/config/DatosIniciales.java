package cl.sonidovivo.serviciostecnicosservice.config;

import cl.sonidovivo.serviciostecnicosservice.model.SolicitudServicio;
import cl.sonidovivo.serviciostecnicosservice.repository.SolicitudServicioRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

// Dos solicitudes de ejemplo de la clienta de prueba (camila@sonidovivo.cl)
// para mostrar el listado y el seguimiento sin tener que crearlas a mano.
@Configuration
public class DatosIniciales {
  @Bean CommandLineRunner solicitudesIniciales(SolicitudServicioRepository repo) {
    return args -> {
      if (repo.findByCorreoIgnoreCase("camila@sonidovivo.cl").isEmpty()) {
        repo.save(new SolicitudServicio(null, "Camila Soto", "camila@sonidovivo.cl",
          "912345678", "guitarra-acustica",
          "Las cuerdas trastean en los primeros trastes.", "PENDIENTE", LocalDate.now()));
        repo.save(new SolicitudServicio(null, "Camila Soto", "camila@sonidovivo.cl",
          "912345678", "bajo",
          "El jack de salida hace ruido al mover el cable.", "EN_REPARACION", LocalDate.now()));
      }
    };
  }
}
