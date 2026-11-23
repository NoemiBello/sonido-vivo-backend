package cl.sonidovivo.productosservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.issuer}")
  private String issuer;

  private SecretKey obtenerClave() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public Claims validarToken(String token) {

    return Jwts.parser()
        .verifyWith(obtenerClave())
        .requireIssuer(issuer)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
