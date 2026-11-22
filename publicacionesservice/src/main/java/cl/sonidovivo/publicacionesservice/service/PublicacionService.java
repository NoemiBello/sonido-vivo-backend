package cl.sonidovivo.publicacionesservice.service;

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
    public List<Publicacion> listar() {
        return publicacionRepository.findAll();
    }

    // BUSCAR PUBLICACION POR ID
    public Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Publicacion no encontrada con ID: " + id));
    }

    // CREAR PUBLICACION
    public Publicacion crear(Publicacion publicacion) {
        publicacion.setId(null);
        return publicacionRepository.save(publicacion);
    }

    // ACTUALIZAR PUBLICACION
    public Publicacion actualizar(Long id, Publicacion publicacion) {

        Publicacion existente = buscarPorId(id);

        existente.setTitulo(publicacion.getTitulo());
        existente.setResumen(publicacion.getResumen());
        existente.setContenido(publicacion.getContenido());
        existente.setFecha(publicacion.getFecha());
        existente.setAutor(publicacion.getAutor());

        return publicacionRepository.save(existente);
    }

    // ELIMINAR PUBLICACION
    public void eliminar(Long id) {
        Publicacion existente = buscarPorId(id);
        publicacionRepository.delete(existente);
    }
}