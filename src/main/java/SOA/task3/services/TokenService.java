package SOA.task3.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Base64;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import java.util.List;

public class TokenService {

	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // 512-bit key
	private static final long EXPIRATION_TIME = 86400000;  // Token validity: 24 hours

    // Generate a JWT token for a given user
    public String generateTokenForUser(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)  // Use the secure key
                .compact();
    }
    
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);  // Extract the roles
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        String tokenUsername = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
