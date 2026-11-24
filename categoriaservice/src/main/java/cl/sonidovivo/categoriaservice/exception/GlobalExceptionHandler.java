package cl.sonidovivo.categoriaservice.exception;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiException.class)
  ResponseEntity<?> api(ApiException e) {
    return ResponseEntity.status(e.status).body(Map.of("mensaje", e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> validation(MethodArgumentNotValidException e) {
    var errors = new java.util.LinkedHashMap<String, String>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));
    return ResponseEntity.badRequest()
        .body(Map.of("mensaje", "Revisa los datos ingresados.", "errores", errors));
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  ResponseEntity<?> format(Exception e) {
    return ResponseEntity.badRequest().body(Map.of("mensaje", "Formato de datos inválido."));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<?> conflict(Exception e) {
    return ResponseEntity.status(409)
        .body(Map.of("mensaje", "El registro ya existe o está relacionado con otros datos."));
  }
}
