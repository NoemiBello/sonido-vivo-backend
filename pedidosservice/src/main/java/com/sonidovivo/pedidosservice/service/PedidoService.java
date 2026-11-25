
package com.sonidovivo.pedidosservice.service;

import com.sonidovivo.pedidosservice.client.ProductosInventarioClient;
import com.sonidovivo.pedidosservice.dto.*;
import com.sonidovivo.pedidosservice.exception.RecursoNoEncontradoException;
import com.sonidovivo.pedidosservice.model.DetallePedido;
import com.sonidovivo.pedidosservice.model.Pedido;
import com.sonidovivo.pedidosservice.repository.PedidoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PedidoService {

        private final PedidoRepository pedidoRepository;
        private final ProductosInventarioClient productosInventarioClient;

        public PedidoService(
                        PedidoRepository pedidoRepository,
                        ProductosInventarioClient productosInventarioClient) {

                this.pedidoRepository = pedidoRepository;
                this.productosInventarioClient = productosInventarioClient;
        }

        // CREAR PEDIDO
        @Transactional
        public PedidoResponse crearPedido(CrearPedidoRequest request, String correo) {

                Pedido pedido = new Pedido();

                pedido.setUsuarioId(request.usuarioId());
                pedido.setCorreoCliente(correo);
                pedido.setNombreCliente(request.nombreCliente());
                pedido.setDireccionEntrega(request.direccionEntrega());
                pedido.setEstado("PENDIENTE");

                BigDecimal total = BigDecimal.ZERO;
                Set<Long> productosAgregados = new HashSet<>();

                for (DetallePedidoRequest item : request.detalles()) {

                        Long productoId = item.productoId();
                        Integer cantidad = item.cantidad();

                        if (productoId == null || productoId <= 0) {
                                throw new IllegalArgumentException(
                                                "El ID del producto debe ser mayor que cero.");
                        }

                        if (cantidad == null || cantidad <= 0) {
                                throw new IllegalArgumentException(
                                                "La cantidad debe ser mayor que cero.");
                        }

                        if (!productosAgregados.add(productoId)) {
                                throw new IllegalArgumentException(
                                                "El producto " + productoId +
                                                                " está repetido en el pedido.");
                        }

                        ProductoResponse producto = productosInventarioClient.obtenerProducto(productoId);

                        if (!Boolean.TRUE.equals(producto.activo())) {
                                throw new IllegalArgumentException(
                                                "El producto " + productoId + " no está activo.");
                        }

                        if (producto.precio() == null ||
                                        producto.precio().compareTo(BigDecimal.ZERO) <= 0) {

                                throw new IllegalArgumentException(
                                                "El producto " + productoId +
                                                                " no tiene un precio válido.");
                        }

                        BigDecimal descuento = producto.descuento() == null
                                        ? BigDecimal.ZERO
                                        : producto.descuento();

                        if (descuento.compareTo(BigDecimal.ZERO) < 0 ||
                                        descuento.compareTo(new BigDecimal("100")) > 0) {

                                throw new IllegalArgumentException(
                                                "El descuento del producto " + productoId +
                                                                " no es válido.");
                        }

                        InventarioResponse inventario = productosInventarioClient.obtenerInventario(productoId);

                        if (inventario.stock() == null ||
                                        inventario.stock() < cantidad) {

                                throw new IllegalArgumentException(
                                                "No hay stock suficiente para el producto " +
                                                                productoId);
                        }

                        BigDecimal factorDescuento = BigDecimal.ONE.subtract(
                                        descuento.divide(
                                                        new BigDecimal("100"),
                                                        6,
                                                        RoundingMode.HALF_UP));

                        BigDecimal precioFinal = producto.precio()
                                        .multiply(factorDescuento)
                                        .setScale(2, RoundingMode.HALF_UP);

                        BigDecimal subtotal = precioFinal.multiply(
                                        BigDecimal.valueOf(cantidad));

                        DetallePedido detalle = new DetallePedido();

                        detalle.setProductoId(productoId);
                        detalle.setCantidad(cantidad);
                        detalle.setPrecioUnitario(precioFinal);
                        detalle.setSubtotal(subtotal);
                        detalle.setPedido(pedido);

                        pedido.getDetalles().add(detalle);

                        total = total.add(subtotal);
                }

                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException(
                                        "El total del pedido debe ser mayor que cero.");
                }

                pedido.setTotal(total);

                // Guardar el pedido en la base de datos
                Pedido guardado = pedidoRepository.saveAndFlush(pedido);

                // Descontar del inventario las unidades compradas
                for (DetallePedido detalle : guardado.getDetalles()) {

                        productosInventarioClient.descontarStock(
                                        detalle.getProductoId(),
                                        detalle.getCantidad());
                }

                return convertirAResponse(guardado);
        }

        // LISTAR TODOS LOS PEDIDOS
        @Transactional(readOnly = true)
        public List<PedidoResponse> listarPedidos() {

                return pedidoRepository.findAll()
                                .stream()
                                .map(this::convertirAResponse)
                                .toList();
        }

        // BUSCAR PEDIDO POR ID
        @Transactional(readOnly = true)
        public PedidoResponse obtenerPedidoPorId(Long id) {

                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "No se encontró el pedido con ID " + id));

                return convertirAResponse(pedido);
        }

        // LISTAR PEDIDOS DE UN USUARIO
        @Transactional(readOnly = true)
        public List<PedidoResponse> listarPedidosPorUsuario(
                        Long usuarioId, String correo, boolean esAdmin) {

                return pedidoRepository.findByUsuarioId(usuarioId)
                                .stream()
                                // El cliente solo ve los pedidos hechos con su cuenta
                                .filter(pedido -> esAdmin
                                                || correo.equals(pedido.getCorreoCliente()))
                                .map(this::convertirAResponse)
                                .toList();
        }

        // ACTUALIZAR DATOS DE ENTREGA (solo mientras está pendiente)
        @Transactional
        public PedidoResponse actualizarPedido(Long id, ActualizarPedidoRequest request) {

            Pedido pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró el pedido con ID " + id));

            if (!"PENDIENTE".equals(pedido.getEstado())) {
                throw new IllegalArgumentException(
                        "Solo se puede modificar un pedido pendiente.");
            }

            pedido.setNombreCliente(request.nombreCliente());
            pedido.setDireccionEntrega(request.direccionEntrega());

            return convertirAResponse(pedidoRepository.save(pedido));
        }

        // ACTUALIZAR ESTADO DEL PEDIDO
        @Transactional
        public PedidoResponse actualizarEstado(Long id, String nuevoEstado) {

                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "No se encontró el pedido con ID " + id));

                if (nuevoEstado == null || nuevoEstado.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El estado del pedido es obligatorio.");
                }

                String estado = nuevoEstado.trim().toUpperCase();

                Map<String, Set<String>> transicionesPermitidas = Map.of(
                                "PENDIENTE",
                                Set.of("EN_PREPARACION", "CANCELADO"),

                                "EN_PREPARACION",
                                Set.of("DESPACHADO", "CANCELADO"),

                                "DESPACHADO",
                                Set.of("ENTREGADO"),

                                "ENTREGADO",
                                Set.of(),

                                "CANCELADO",
                                Set.of());

                String estadoActual = pedido.getEstado();

                if (!transicionesPermitidas.containsKey(estadoActual)) {
                        throw new IllegalArgumentException(
                                        "El pedido tiene un estado actual no válido.");
                }

                if (estadoActual.equals(estado)) {
                        return convertirAResponse(pedido);
                }

                if (!transicionesPermitidas.get(estadoActual).contains(estado)) {
                        throw new IllegalArgumentException(
                                        "No se puede cambiar el estado de " +
                                                        estadoActual + " a " + estado + ".");
                }

                // Si se cancela el pedido, devolver los productos al inventario
                if ("CANCELADO".equals(estado)) {

                        for (DetallePedido detalle : pedido.getDetalles()) {

                                productosInventarioClient.reponerStock(
                                                detalle.getProductoId(),
                                                detalle.getCantidad());
                        }
                }

                pedido.setEstado(estado);

                Pedido actualizado = pedidoRepository.save(pedido);

                return convertirAResponse(actualizado);
        }

        // ELIMINAR PEDIDO
        @Transactional
        public void eliminarPedido(Long id) {
                actualizarEstado(id, "CANCELADO");
        }

        // CONVERTIR PEDIDO A DTO DE RESPUESTA
        private PedidoResponse convertirAResponse(Pedido pedido) {

                List<DetallePedidoResponse> detalles = pedido.getDetalles()
                                .stream()
                                .map(this::convertirDetalleAResponse)
                                .toList();

                return new PedidoResponse(
                                pedido.getId(),
                                pedido.getUsuarioId(),
                                pedido.getNombreCliente(),
                                pedido.getDireccionEntrega(),
                                pedido.getTotal(),
                                pedido.getEstado(),
                                pedido.getFechaCreacion(),
                                detalles);
        }

        // CONVERTIR DETALLE A DTO DE RESPUESTA
        private DetallePedidoResponse convertirDetalleAResponse(
                        DetallePedido detalle) {

                return new DetallePedidoResponse(
                                detalle.getId(),
                                detalle.getProductoId(),
                                detalle.getCantidad(),
                                detalle.getPrecioUnitario(),
                                detalle.getSubtotal());
        }
}
