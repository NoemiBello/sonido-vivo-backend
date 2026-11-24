package cl.sonidovivo.inventarioservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorization = request.getHeader("Authorization");

    if (authorization != null && authorization.startsWith("Bearer ")) {

      String token = authorization.substring(7);

      try {
        Claims claims = jwtService.validarToken(token);

        String correo = claims.getSubject();
        String rol = claims.get("rol", String.class);

        if (correo != null
            && !correo.isBlank()
            && rol != null
            && !rol.isBlank()
            && claims.getExpiration() != null) {

          var autenticacion =
              new UsernamePasswordAuthenticationToken(
                  correo, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));

          SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

      } catch (JwtException | IllegalArgumentException ex) {
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }
}
