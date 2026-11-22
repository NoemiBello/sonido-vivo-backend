
package com.sonidovivo.pedidosservice.controller;

import com.sonidovivo.pedidosservice.dto.CrearPedidoRequest;
import com.sonidovivo.pedidosservice.dto.PedidoResponse;
import com.sonidovivo.pedidosservice.service.PedidoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request) {

        PedidoResponse pedido = pedidoService.crearPedido(request);

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
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                pedidoService.listarPedidosPorUsuario(usuarioId)
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
