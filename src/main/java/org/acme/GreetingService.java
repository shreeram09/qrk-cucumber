package org.acme;

import org.jboss.logging.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    
    private static final Logger log = Logger.getLogger(GreetingService.class);

    private static final String SECRET_KEY = "your-256-bit-secret"; // Replace with your actual secret

    public boolean isAuthorized(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            log.info("no auth header.");
            return false;
        }
        String[] parts = authHeader.split(" ", 2);
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            log.info("Invalid auth header format.");
            return false;
        }
        String token = parts[1];
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
            // Optionally, check claims for user/roles/expiry
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    public String createGreeting(UserRequest userRequest) {
        return String.format("Hello %s, age %d!", userRequest.name, userRequest.age);
    }
}
