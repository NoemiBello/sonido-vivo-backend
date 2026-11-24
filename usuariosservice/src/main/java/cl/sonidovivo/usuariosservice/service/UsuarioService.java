package cl.sonidovivo.usuariosservice.service;

import cl.sonidovivo.usuariosservice.exception.DatosInvalidosException;
import cl.sonidovivo.usuariosservice.exception.RecursoNoEncontradoException;
import cl.sonidovivo.usuariosservice.model.Usuario;
import cl.sonidovivo.usuariosservice.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreoIgnoreCase(correo).orElseThrow(() ->
            new RecursoNoEncontradoException("Usuario no encontrado: " + correo));
    }

    // Un correo solo puede tener un registro de datos personales.
    public Usuario guardar(Usuario usuario) {
        if (repository.findByCorreoIgnoreCase(usuario.getCorreo()).isPresent()) {
            throw new DatosInvalidosException("El correo ya tiene datos registrados");
        }
        // El id lo asigna la base de datos: si llegara uno en el JSON,
        // save() modificaría la ficha de otra persona en vez de crear una nueva.
        usuario.setId(null);
        usuario.setCorreo(usuario.getCorreo().trim().toLowerCase());
        return repository.save(usuario);
    }

    // El correo no se modifica: es el vínculo con la cuenta en authservice.
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario actual = buscar(id);
        actual.setNombre(datos.getNombre());
        actual.setApellido(datos.getApellido());
        actual.setTelefono(datos.getTelefono());
        actual.setDireccion(datos.getDireccion());
        return repository.save(actual);
    }

    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }
}
