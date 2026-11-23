package cl.sonidovivo.serviciostecnicosservice.repository;

import cl.sonidovivo.serviciostecnicosservice.model.SolicitudServicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudServicioRepository extends JpaRepository<SolicitudServicio, Long> {
    List<SolicitudServicio> findByCorreoIgnoreCase(String correo);
}
