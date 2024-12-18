package com.example.bmillion_backend.core.security;

import com.example.bmillion_backend.core.error.ErrorCode;
import com.example.bmillion_backend.core.error.exception.NotFoundException;
import com.example.bmillion_backend.core.error.exception.UnAuthorizedException;
import com.example.bmillion_backend.entity.UserEntity;
import com.example.bmillion_backend.entity.custom.UserRole;
import com.example.bmillion_backend.repo.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailService customUserDetailService;
    private final UserRepo userRepo;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenValidTime;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenValidTime;

    public String resolveAT(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    public String resolveRT(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new UnAuthorizedException("토큰 만료", ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public Map<String, String> getTokenBody(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Map<String, String> claimsBody = new HashMap<>();
        claimsBody.put("role", (String) claims.get("role"));
        claimsBody.put("userImg", (String) claims.get("userImg"));
        return claimsBody;
    }

    public String createAccessToken(String userId, UserRole userRole) {

        return this.createToken(userId, userRole, accessTokenValidTime);
    }

    public String createRefreshToken(String userId, UserRole userRole) {
        return this.createToken(userId, userRole, refreshTokenValidTime);
    }

    private String createToken(String userId, UserRole userRole, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", userRole.toString());
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        this.validateToken(refreshToken);

        UserEntity userEntity = userRepo.findByUserId(this.getUserId(refreshToken))
                .orElseThrow(()-> new NotFoundException("force re-login", ErrorCode.NOT_FOUND_EXCEPTION));

        if (!userEntity.getRefreshToken().equals(refreshToken))
            throw new UnAuthorizedException("force re-login.",ErrorCode.UNAUTHORIZED_EXCEPTION);

        return this.createAccessToken(userEntity.getUserId(), userEntity.getUserRole());
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "Bearer "+ accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "Bearer "+ refreshToken);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

}