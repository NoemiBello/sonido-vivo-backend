package cl.sonidovivo.serviciostecnicosservice.controller;

import cl.sonidovivo.serviciostecnicosservice.model.SolicitudServicio;
import cl.sonidovivo.serviciostecnicosservice.service.SolicitudServicioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<SolicitudServicio> buscarPorCorreo(@PathVariable String correo) {
        return service.buscarPorCorreo(correo);
    }

    @PostMapping
    public ResponseEntity<SolicitudServicio> crear(@Valid @RequestBody SolicitudServicio solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(solicitud));
    }
}
