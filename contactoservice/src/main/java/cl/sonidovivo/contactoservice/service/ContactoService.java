package cl.sonidovivo.contactoservice.service;

import cl.sonidovivo.contactoservice.model.Contacto;
import cl.sonidovivo.contactoservice.repository.ContactoRepository;
import cl.sonidovivo.contactoservice.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContactoService {

    private final ContactoRepository repository;

    public ContactoService(ContactoRepository repository) {
        this.repository = repository;
    }

    public List<Contacto> listar() {
        return repository.findAll();
    }

    public Contacto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Contacto no encontrado con ID: " + id));
    }

    public Contacto crear(Contacto contacto) {
        contacto.setId(null);
        return repository.save(contacto);
    }

    public Contacto actualizar(Long id, Contacto datos) {


        
        Contacto contacto = buscarPorId(id);

        contacto.setNombre(datos.getNombre());
        contacto.setCorreo(datos.getCorreo());
        contacto.setAsunto(datos.getAsunto());
        contacto.setMensaje(datos.getMensaje());

        return repository.save(contacto);
    }

    public void eliminar(Long id) {
        Contacto contacto = buscarPorId(id);
        repository.delete(contacto);
    }
}