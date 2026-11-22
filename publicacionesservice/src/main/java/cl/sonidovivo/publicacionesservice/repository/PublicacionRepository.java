package cl.sonidovivo.publicacionesservice.repository;

import cl.sonidovivo.publicacionesservice.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository
        extends JpaRepository<Publicacion, Long> {

}