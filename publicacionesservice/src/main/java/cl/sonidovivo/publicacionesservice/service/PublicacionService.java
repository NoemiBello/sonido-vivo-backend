
package cl.sonidovivo.publicacionesservice.service;

import cl.sonidovivo.publicacionesservice.dto.PublicacionRequest;
import cl.sonidovivo.publicacionesservice.dto.PublicacionResponse;
import cl.sonidovivo.publicacionesservice.model.Publicacion;
import cl.sonidovivo.publicacionesservice.repository.PublicacionRepository;
import cl.sonidovivo.publicacionesservice.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    // LISTAR PUBLICACIONES
    public List<PublicacionResponse> listar() {
        return publicacionRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // BUSCAR PUBLICACION POR ID
    public PublicacionResponse buscarPorId(Long id) {
        return convertirAResponse(buscarEntidadPorId(id));
    }

    // CREAR PUBLICACION
    public PublicacionResponse crear(PublicacionRequest request) {

        Publicacion publicacion = new Publicacion();

        publicacion.setTitulo(request.titulo());
        publicacion.setResumen(request.resumen());
        publicacion.setContenido(request.contenido());
        publicacion.setFecha(request.fecha());
        publicacion.setAutor(request.autor());

        return convertirAResponse(publicacionRepository.save(publicacion));
    }

    // ACTUALIZAR PUBLICACION
    public PublicacionResponse actualizar(Long id, PublicacionRequest request) {

        Publicacion existente = buscarEntidadPorId(id);

        existente.setTitulo(request.titulo());
        existente.setResumen(request.resumen());
        existente.setContenido(request.contenido());
        existente.setFecha(request.fecha());
        existente.setAutor(request.autor());

        return convertirAResponse(publicacionRepository.save(existente));
    }

    // ELIMINAR PUBLICACION
    public void eliminar(Long id) {
        Publicacion existente = buscarEntidadPorId(id);
        publicacionRepository.delete(existente);
    }

    private Publicacion buscarEntidadPorId(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Publicacion no encontrada con ID: " + id));
    }

    private PublicacionResponse convertirAResponse(Publicacion publicacion) {
        return new PublicacionResponse(
                publicacion.getId(),
                publicacion.getTitulo(),
                publicacion.getResumen(),
                publicacion.getContenido(),
                publicacion.getFecha(),
                publicacion.getAutor()
        );
    }
}
