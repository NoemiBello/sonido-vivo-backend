package cl.sonidovivo.authservice.exception;

// Regla de negocio incumplida (correo repetido, rol inexistente, clave corta).
public class DatosInvalidosException extends RuntimeException {
  public DatosInvalidosException(String mensaje) {
    super(mensaje);
  }
}
