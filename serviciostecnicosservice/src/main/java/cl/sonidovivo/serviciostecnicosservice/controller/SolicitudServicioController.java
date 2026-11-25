package cl.sonidovivo.serviciostecnicosservice.controller;

import cl.sonidovivo.serviciostecnicosservice.dto.CambioEstadoRequest;
import cl.sonidovivo.serviciostecnicosservice.exception.AccesoDenegadoException;
import cl.sonidovivo.serviciostecnicosservice.model.SolicitudServicio;
import cl.sonidovivo.serviciostecnicosservice.service.SolicitudServicioService;
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
@RequestMapping("/api/v1/servicios-tecnicos")
public class SolicitudServicioController {
    private final SolicitudServicioService service;

    public SolicitudServicioController(SolicitudServicioService service) {
        this.service = service;
    }

    @GetMapping public List<SolicitudServicio> listar() { return service.listar(); }

    @GetMapping("/{id}") public SolicitudServicio buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping("/correo/{correo}")
    public List<SolicitudServicio> buscarPorCorreo(@PathVariable String correo, Authentication auth) {
        validarPropietario(correo, auth);
        return service.buscarPorCorreo(correo);
    }

    @PostMapping
    public ResponseEntity<SolicitudServicio> crear(@Valid @RequestBody SolicitudServicio solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(solicitud));
    }

    @PutMapping("/{id}")
    public SolicitudServicio actualizar(@PathVariable Long id,
        @Valid @RequestBody SolicitudServicio solicitud) {
        return service.actualizar(id, solicitud);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }

    @PutMapping("/{id}/estado")
    public SolicitudServicio cambiarEstado(@PathVariable Long id,
        @Valid @RequestBody CambioEstadoRequest request) {
        return service.cambiarEstado(id, request);
    }

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
