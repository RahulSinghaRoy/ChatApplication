//package com.jwt;
//
//import java.util.Date;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Component
//public class JwtUtil {
//
//	private String SECRET_KEY = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody().getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//    
//    // Overloaded method for JwtHandshakeInterceptor
//    public boolean validateToken(String token) {
//        final String username = extractUsername(token);
//        return (username != null && !isTokenExpired(token));
//    }
//
//
//    private boolean isTokenExpired(String token) {
//        return Jwts.parser().setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
//    }
//}
