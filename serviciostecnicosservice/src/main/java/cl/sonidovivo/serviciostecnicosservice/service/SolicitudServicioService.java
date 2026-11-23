package cl.sonidovivo.serviciostecnicosservice.service;

import cl.sonidovivo.serviciostecnicosservice.dto.CambioEstadoRequest;
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

    // Etapas de la reparación, en orden. Una solicitud solo avanza a la siguiente.
    private static final String[] ESTADOS = {
        "PENDIENTE", "EN_REVISION", "EN_REPARACION", "LISTO", "ENTREGADO"
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

    // Corrige los datos de contacto o del instrumento.
    // El estado y la fecha no se cambian aquí: el estado tiene su propio flujo.
    public SolicitudServicio actualizar(Long id, SolicitudServicio datos) {
        validarInstrumento(datos.getInstrumento());
        SolicitudServicio actual = buscar(id);
        actual.setNombre(datos.getNombre());
        actual.setCorreo(datos.getCorreo().trim().toLowerCase());
        actual.setTelefono(datos.getTelefono());
        actual.setInstrumento(datos.getInstrumento());
        actual.setDescripcion(datos.getDescripcion());
        return repository.save(actual);
    }

    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    // Regla de negocio: el estado avanza paso a paso y no retrocede.
    // ENTREGADO es el estado final.
    public SolicitudServicio cambiarEstado(Long id, CambioEstadoRequest request) {
        SolicitudServicio solicitud = buscar(id);
        String nuevo = request.estado().trim().toUpperCase();

        int posicionActual = posicionDe(solicitud.getEstado());
        int posicionNueva = posicionDe(nuevo);

        if (posicionNueva == -1) {
            throw new DatosInvalidosException(
                "Estado no válido: use PENDIENTE, EN_REVISION, EN_REPARACION, LISTO o ENTREGADO");
        }
        if (posicionActual == ESTADOS.length - 1) {
            throw new DatosInvalidosException("La solicitud ya fue entregada");
        }
        if (posicionNueva != posicionActual + 1) {
            throw new DatosInvalidosException(
                "Desde " + solicitud.getEstado() + " solo se puede pasar a " + ESTADOS[posicionActual + 1]);
        }
        solicitud.setEstado(nuevo);
        return repository.save(solicitud);
    }

    private int posicionDe(String estado) {
        for (int i = 0; i < ESTADOS.length; i++) {
            if (ESTADOS[i].equals(estado)) {
                return i;
            }
        }
        return -1;
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
