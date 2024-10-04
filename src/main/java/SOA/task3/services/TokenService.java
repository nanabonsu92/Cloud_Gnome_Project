package SOA.task3.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Base64;
import javax.crypto.SecretKey;

public class TokenService {

	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // 512-bit key
	private static final long EXPIRATION_TIME = 86400000;  // Token validity: 24 hours

    // Generate a JWT token for a given user
    public String generateTokenForUser(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)  // Use the secure key
                .compact();
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
