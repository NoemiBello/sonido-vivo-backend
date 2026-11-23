package cl.sonidovivo.usuariosservice.exception;

// Regla de negocio incumplida (por ejemplo, correo ya registrado).
public class DatosInvalidosException extends RuntimeException {
  public DatosInvalidosException(String mensaje) {
    super(mensaje);
  }
}
