package cl.sonidovivo.authservice.service;

import cl.sonidovivo.authservice.dto.*;
import cl.sonidovivo.authservice.exception.DatosInvalidosException;
import cl.sonidovivo.authservice.exception.RecursoNoEncontradoException;
import cl.sonidovivo.authservice.model.Usuario;
import cl.sonidovivo.authservice.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Administración de cuentas: crear, editar rol/estado y desactivar.
@Service
public class CredencialService {
  private final UsuarioRepository usuarios;
  private final PasswordEncoder encoder;

  public CredencialService(UsuarioRepository usuarios, PasswordEncoder encoder) {
    this.usuarios = usuarios;
    this.encoder = encoder;
  }

  public List<CredencialResponse> listar() {
    List<CredencialResponse> lista = new ArrayList<>();
    for (Usuario usuario : usuarios.findAll()) {
      lista.add(aRespuesta(usuario));
    }
    return lista;
  }

  public CredencialResponse buscar(Long id) {
    return aRespuesta(obtener(id));
  }

  public CredencialResponse crear(CredencialRequest request) {
    if (usuarios.findByCorreoIgnoreCase(request.correo()).isPresent()) {
      throw new DatosInvalidosException("El correo ya está registrado");
    }
    if (request.password().length() < 6) {
      throw new DatosInvalidosException("La contraseña debe tener al menos 6 caracteres");
    }
    Usuario usuario = new Usuario();
    usuario.setCorreo(request.correo().trim().toLowerCase());
    usuario.setPassword(encoder.encode(request.password()));
    usuario.setRol(validarRol(request.rol()));
    usuario.setActivo(true);
    return aRespuesta(usuarios.save(usuario));
  }

  public CredencialResponse actualizar(Long id, ActualizarCredencialRequest request) {
    Usuario usuario = obtener(id);
    String rol = validarRol(request.rol());
    boolean activo = usuario.isActivo();
    if (request.activo() != null) {
      activo = request.activo();
    }
    // Si deja de ser un administrador activo, debe quedar otro en el sistema.
    if (!rol.equals("ADMIN") || !activo) {
      validarUltimoAdministrador(usuario);
    }
    usuario.setRol(rol);
    usuario.setActivo(activo);
    return aRespuesta(usuarios.save(usuario));
  }

  // No se borra el registro: se desactiva y ya no puede iniciar sesión.
  public void desactivar(Long id) {
    Usuario usuario = obtener(id);
    validarUltimoAdministrador(usuario);
    usuario.setActivo(false);
    usuarios.save(usuario);
  }

  private Usuario obtener(Long id) {
    return usuarios.findById(id).orElseThrow(() ->
      new RecursoNoEncontradoException("Usuario no encontrado: " + id));
  }

  // Regla de negocio: el sistema no puede quedar sin administradores activos.
  private void validarUltimoAdministrador(Usuario usuario) {
    if (!usuario.getRol().equals("ADMIN") || !usuario.isActivo()) {
      return;
    }
    int administradores = 0;
    for (Usuario otro : usuarios.findAll()) {
      if (otro.getRol().equals("ADMIN") && otro.isActivo()) {
        administradores++;
      }
    }
    if (administradores == 1) {
      throw new DatosInvalidosException("Debe quedar al menos un administrador activo");
    }
  }

  private String validarRol(String rol) {
    String valor = rol.trim().toUpperCase();
    if (!valor.equals("ADMIN") && !valor.equals("VENDEDOR") && !valor.equals("USER")) {
      throw new DatosInvalidosException("Rol no válido: use ADMIN, VENDEDOR o USER");
    }
    return valor;
  }

  private CredencialResponse aRespuesta(Usuario usuario) {
    return new CredencialResponse(usuario.getId(), usuario.getCorreo(),
      usuario.getRol(), usuario.isActivo());
  }
}
