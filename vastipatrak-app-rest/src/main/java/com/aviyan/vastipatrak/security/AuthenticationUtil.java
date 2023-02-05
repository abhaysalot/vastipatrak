package com.aviyan.vastipatrak.security;

import com.aviyan.vastipatrak.model.LoggedInUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class AuthenticationUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtil.class);

    @Value("${aviyan.app.jwtSecret}")
    private String jwtSecret;

    @Value("${aviyan.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        LoggedInUser loggedInUserPrincipal = new LoggedInUser();
        loggedInUserPrincipal.setUsername(String.valueOf(authentication.getPrincipal()));

        return Jwts.builder()
                .setSubject((loggedInUserPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String refreshJwtTokenFromExistingToken(String existingToken) {

        if(validateJwtToken(existingToken)){
            Date expiryDateTime = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(existingToken).getBody().getExpiration();
            //If the expiry is within an hour then refresh the token else return the existing token
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            if(expiryDateTime.before(calendar.getTime())){
                //Generate new token
                return Jwts.builder()
                        .setSubject(getUserNameFromJwtToken(existingToken))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .compact();
            }
            //Send existing token
            return existingToken;
        }
        return null;
    }

        public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}