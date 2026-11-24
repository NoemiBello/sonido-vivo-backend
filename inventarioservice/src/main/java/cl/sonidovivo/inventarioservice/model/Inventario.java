package cl.sonidovivo.inventarioservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inventario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long productoId;

  @Column(nullable = false)
  private Integer stock;

  @Column(nullable = false)
  private Integer stockCritico;
}
