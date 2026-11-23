package cl.sonidovivo.serviciostecnicosservice.exception;

// Regla de negocio incumplida (instrumento no atendido, cambio de estado no permitido).
public class DatosInvalidosException extends RuntimeException {
  public DatosInvalidosException(String mensaje) {
    super(mensaje);
  }
}
