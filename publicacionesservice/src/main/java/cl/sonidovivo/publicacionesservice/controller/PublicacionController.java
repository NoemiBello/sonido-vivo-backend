package cl.sonidovivo.publicacionesservice.controller;

import cl.sonidovivo.publicacionesservice.model.Publicacion;
import cl.sonidovivo.publicacionesservice.service.PublicacionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    // LISTAR PUBLICACIONES
    @GetMapping
    public ResponseEntity<List<Publicacion>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    // BUSCAR PUBLICACION POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                publicacionService.buscarPorId(id));
    }

    // CREAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<Publicacion> crear(
            @Valid @RequestBody Publicacion publicacion) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicacionService.crear(publicacion));
    }

    // ACTUALIZAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Publicacion publicacion) {

        return ResponseEntity.ok(
                publicacionService.actualizar(id, publicacion));
    }

    // ELIMINAR PUBLICACION
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        publicacionService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}