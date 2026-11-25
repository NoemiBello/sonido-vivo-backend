
package com.sonidovivo.pedidosservice.controller;

import com.sonidovivo.pedidosservice.dto.ActualizarPedidoRequest;
import com.sonidovivo.pedidosservice.dto.CrearPedidoRequest;
import com.sonidovivo.pedidosservice.dto.PedidoResponse;
import com.sonidovivo.pedidosservice.service.PedidoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request,
            Authentication auth) {

        PedidoResponse pedido = pedidoService.crearPedido(request, auth.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedido);
    }

    // Consultar todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {

        return ResponseEntity.ok(
                pedidoService.listarPedidos()
        );
    }

    // Consultar un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPedido(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pedidoService.obtenerPedidoPorId(id)
        );
    }

    // Consultar los pedidos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponse>> listarPorUsuario(
            @PathVariable Long usuarioId,
            Authentication auth) {

        boolean esAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return ResponseEntity.ok(
                pedidoService.listarPedidosPorUsuario(
                        usuarioId, auth.getName(), esAdmin)
        );
    }

    // Actualizar los datos de entrega de un pedido
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPedidoRequest request) {

        return ResponseEntity.ok(
                pedidoService.actualizarPedido(id, request)
        );
    }

    // Actualizar el estado de un pedido
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        return ResponseEntity.ok(
                pedidoService.actualizarEstado(id, estado)
        );
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(
            @PathVariable Long id) {

        pedidoService.eliminarPedido(id);

        return ResponseEntity.noContent().build();
    }



}
