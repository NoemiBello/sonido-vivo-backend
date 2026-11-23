package cl.sonidovivo.productosservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Producto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 40)
  private String codigo;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(length = 500)
  private String descripcion;

  @Column(length = 500)
  private String imagen;

  @Column(nullable = false)
  private Long categoriaId;

  @Column(nullable = false, precision = 14, scale = 2)
  private java.math.BigDecimal precio;

  @Column(nullable = false)
  private Integer descuento;

  @Column(nullable = false)
  private boolean activo = true;
}
