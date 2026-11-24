
package com.sonidovivo.pedidosservice.client;

import com.sonidovivo.pedidosservice.dto.ProductoResponse;
import com.sonidovivo.pedidosservice.dto.InventarioResponse;
import com.sonidovivo.pedidosservice.exception.RecursoNoEncontradoException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Map;

@Service
public class ProductosInventarioClient {

    private final RestClient productosClient;
    private final RestClient inventarioClient;

    public ProductosInventarioClient(
            @Value("${app.productos.url}") String productosUrl,
            @Value("${app.inventario.url}") String inventarioUrl) {

        this.productosClient = RestClient.builder()
                .baseUrl(productosUrl)
                .build();

        this.inventarioClient = RestClient.builder()
                .baseUrl(inventarioUrl)
                .build();
    }

    public ProductoResponse obtenerProducto(Long productoId) {

        validarProductoId(productoId);

        try {
            ProductoResponse producto = productosClient.get()
                    .uri("/{id}", productoId)
                    .retrieve()
                    .onStatus(
                            estado -> estado.value() == 404,
                            (request, response) -> {
                                throw new RecursoNoEncontradoException(
                                        "No existe el producto con ID " + productoId);
                            })
                    .body(ProductoResponse.class);

            if (producto == null) {
                throw new IllegalStateException(
                        "Productos Service no devolvió información.");
            }

            return producto;

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "No fue posible consultar Productos Service.", e);
        }
    }

    public InventarioResponse obtenerInventario(Long productoId) {

        validarProductoId(productoId);

        try {
            InventarioResponse inventario = inventarioClient.get()
                    .uri("/producto/{id}", productoId)
                    .retrieve()
                    .onStatus(
                            estado -> estado.value() == 404,
                            (request, response) -> {
                                throw new RecursoNoEncontradoException(
                                        "No se encontró inventario para el producto "
                                                + productoId);
                            })
                    .body(InventarioResponse.class);

            if (inventario == null) {
                throw new IllegalStateException(
                        "Inventario Service no devolvió información.");
            }

            return inventario;

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "No fue posible consultar Inventario Service.", e);
        }
    }

    public InventarioResponse descontarStock(Long productoId, Integer cantidad) {

        validarProductoId(productoId);

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        var atributos = RequestContextHolder.getRequestAttributes();

        if (!(atributos instanceof ServletRequestAttributes servletAttributes)) {
            throw new IllegalStateException(
                    "No se encontró la solicitud HTTP.");
        }

        HttpServletRequest request = servletAttributes.getRequest();
        String autorizacion = request.getHeader("Authorization");

        if (autorizacion == null || !autorizacion.startsWith("Bearer ")) {
            throw new IllegalStateException(
                    "No se encontró el token JWT.");
        }

        try {
            InventarioResponse inventario = inventarioClient.post()
                    .uri("/producto/{id}/movimientos", productoId)
                    .header("Authorization", autorizacion)
                    .body(Map.of("cantidad", -cantidad))
                    .retrieve()
                    .body(InventarioResponse.class);

            if (inventario == null) {
                throw new IllegalStateException(
                        "Inventario Service no devolvió información.");
            }

            return inventario;

        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "No fue posible descontar el stock en Inventario Service.", e);
        }
    }

    public InventarioResponse reponerStock(Long productoId, Integer cantidad) {

        validarProductoId(productoId);

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        var atributos = RequestContextHolder.getRequestAttributes();

        if (!(atributos instanceof ServletRequestAttributes servletAttributes)) {
            throw new IllegalStateException(
                    "No se encontró la solicitud HTTP.");
        }

        HttpServletRequest request = servletAttributes.getRequest();
        String autorizacion = request.getHeader("Authorization");

        if (autorizacion == null || !autorizacion.startsWith("Bearer ")) {
            throw new IllegalStateException(
                    "No se encontró el token JWT.");
        }

        try {
            InventarioResponse inventario = inventarioClient.post()
                    .uri("/producto/{id}/movimientos", productoId)
                    .header("Authorization", autorizacion)
                    .body(Map.of("cantidad", cantidad))
                    .retrieve()
                    .body(InventarioResponse.class);

            if (inventario == null) {
                throw new IllegalStateException(
                        "Inventario Service no devolvió información.");
            }

            return inventario;

        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "No fue posible reponer el stock en Inventario Service.", e);
        }
    }

    private void validarProductoId(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException(
                    "El ID del producto debe ser mayor que cero.");
        }
    }
}
