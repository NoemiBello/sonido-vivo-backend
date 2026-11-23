package cl.sonidovivo.serviciostecnicosservice.service;

import cl.sonidovivo.serviciostecnicosservice.exception.DatosInvalidosException;
import cl.sonidovivo.serviciostecnicosservice.exception.RecursoNoEncontradoException;
import cl.sonidovivo.serviciostecnicosservice.model.SolicitudServicio;
import cl.sonidovivo.serviciostecnicosservice.repository.SolicitudServicioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SolicitudServicioService {
    // Instrumentos que la tienda repara (los del formulario de la página Servicios).
    private static final String[] INSTRUMENTOS = {
        "guitarra-electrica", "guitarra-acustica", "bajo", "otro"
    };

    private final SolicitudServicioRepository repository;

    public SolicitudServicioService(SolicitudServicioRepository repository) {
        this.repository = repository;
    }

    public List<SolicitudServicio> listar() {
        return repository.findAll();
    }

    public SolicitudServicio buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new RecursoNoEncontradoException("Solicitud no encontrada: " + id));
    }

    public List<SolicitudServicio> buscarPorCorreo(String correo) {
        return repository.findByCorreoIgnoreCase(correo);
    }

    // El cliente solo envía sus datos y el problema; el servidor decide
    // el id, el estado inicial y la fecha.
    public SolicitudServicio crear(SolicitudServicio solicitud) {
        validarInstrumento(solicitud.getInstrumento());
        solicitud.setId(null);
        solicitud.setCorreo(solicitud.getCorreo().trim().toLowerCase());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDate.now());
        return repository.save(solicitud);
    }

    private void validarInstrumento(String instrumento) {
        for (String permitido : INSTRUMENTOS) {
            if (permitido.equals(instrumento)) {
                return;
            }
        }
        throw new DatosInvalidosException(
            "Instrumento no atendido: use guitarra-electrica, guitarra-acustica, bajo u otro");
    }
}
