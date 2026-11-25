package cl.sonidovivo.usuariosservice.controller;

import cl.sonidovivo.usuariosservice.exception.AccesoDenegadoException;
import cl.sonidovivo.usuariosservice.model.Usuario;
import cl.sonidovivo.usuariosservice.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping public List<Usuario> listar() { return service.listar(); }

    @GetMapping("/{id}") public Usuario buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping("/correo/{correo}")
    public Usuario buscarPorCorreo(@PathVariable String correo, Authentication auth) {
        validarPropietario(correo, auth);
        return service.buscarPorCorreo(correo);
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario, Authentication auth) {
        validarPropietario(usuario.getCorreo(), auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario,
            Authentication auth) {
        validarPropietario(service.buscar(id).getCorreo(), auth);
        return service.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    // Privacidad: cada usuario accede solo a sus datos y el ADMIN, a todos.
    // La identidad viene del JWT, la registró JwtAuthenticationFilter.
    private void validarPropietario(String correo, Authentication auth) {
        boolean esAdmin = auth.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!esAdmin && !auth.getName().equalsIgnoreCase(correo)) {
            throw new AccesoDenegadoException("Solo puedes acceder a tus propios datos");
        }
    }
}
