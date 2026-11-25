package cl.sonidovivo.serviciostecnicosservice.exception;

// Un usuario intenta ver o modificar datos que no le pertenecen.
public class AccesoDenegadoException extends RuntimeException {
  public AccesoDenegadoException(String mensaje) {
    super(mensaje);
  }
}
