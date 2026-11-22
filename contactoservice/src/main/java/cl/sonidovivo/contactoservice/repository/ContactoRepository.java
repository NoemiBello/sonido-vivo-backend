package cl.sonidovivo.contactoservice.repository;

import cl.sonidovivo.contactoservice.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

}