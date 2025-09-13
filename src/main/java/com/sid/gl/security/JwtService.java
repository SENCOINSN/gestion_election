package com.sid.gl.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sid.gl.users.Role;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;

    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        User user = userRepository.findByUsernameOrEmail(username,username).get();
        if(user !=null){
            claims.put("subject",username);
            claims.put("roles",user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
            return createToken(claims);
        }
        return null;
    }

    private String createToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)  {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token){
        final String username = extractUserName(token);
        Optional <User> optionalUser = userRepository.findByUsernameOrEmail(username,username);
        return optionalUser.isPresent() && !isTokenExpired(token);
    }


    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder().
                setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
