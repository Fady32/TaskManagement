package banquemisr.challenge05.TaskManagementSystem.config;

import banquemisr.challenge05.TaskManagementSystem.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtTokenUtil {

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    private static PrivateKey privateKey;

    @PostConstruct
    private void init(){

        generateRSAKeyPair();
    }

    public String generateToken(UserDetails userDetails, Set<String> roles) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", new ArrayList<>(roles))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(privateKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public void generateRSAKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 2048-bit RSA key size
            privateKey =  keyPairGenerator.generateKeyPair().getPrivate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate token ", e);
        }

    }
}