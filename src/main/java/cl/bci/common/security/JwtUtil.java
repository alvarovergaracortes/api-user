package cl.bci.common.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;

	private Key key;
	private JwtParser parser;
	
	@PostConstruct
	public void init() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		
		if (keyBytes.length < 32) {
			throw new IllegalArgumentException("La clave secreta debe tener minimo 32 caracteres para el algoritmo HS256.");
		}
		
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }
	
	public String generateToken(String email, List<String> roles) {
		return Jwts.builder()
				.setSubject(email)
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		Claims c = parser.parseClaimsJws(token).getBody();
	    return c.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
	      Claims c = parser.parseClaimsJws(token).getBody();
	      Date exp = c.getExpiration();
	      return exp == null || exp.after(new Date());
	    } catch (JwtException | IllegalArgumentException e) {
	      return false;
	    }
	}

	public List<String> extractRoles(String token) {
		Claims c = parser.parseClaimsJws(token).getBody();

	    Object obj = Optional.ofNullable(c.get("roles"))
	                .orElse(Optional.ofNullable(c.get("authorities"))
	                .orElse(c.get("scope")));

	    return toStringList(obj);
	}
	
	private static List<String> toStringList(Object obj) {
	    if (obj == null) return List.of();

	    if (obj instanceof Collection<?> col) {
	      return col.stream()
	          .map(String::valueOf)
	          .map(String::trim)
	          .filter(StringUtils::hasText)
	          .collect(Collectors.toList());
	    }
	    if (obj.getClass().isArray()) {
	      return Arrays.stream((Object[]) obj)
	          .map(String::valueOf)
	          .map(String::trim)
	          .filter(StringUtils::hasText)
	          .collect(Collectors.toList());
	    }
	    String s = String.valueOf(obj).trim();
	    if (!StringUtils.hasText(s)) return List.of();

	    // Soporta CSV o separados por espacios
	    String[] parts = s.contains(",") ? s.split(",") : s.split("\\s+");
	    return Arrays.stream(parts)
	        .map(String::trim)
	        .filter(StringUtils::hasText)
	        .collect(Collectors.toList());
	  }
}
