package cl.sonidovivo.contactoservice.controller;

import cl.sonidovivo.contactoservice.model.Contacto;
import cl.sonidovivo.contactoservice.service.ContactoService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    private final ContactoService service;

    public ContactoController(ContactoService service) {
        this.service = service;
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Contacto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<Contacto> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Contacto> crear(
            @Valid @RequestBody Contacto contacto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crear(contacto));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Contacto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Contacto contacto) {
        return ResponseEntity.ok(
                service.actualizar(id, contacto));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}