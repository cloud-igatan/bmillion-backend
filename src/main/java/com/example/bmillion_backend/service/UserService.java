package com.example.bmillion_backend.service;

import com.example.bmillion_backend.core.error.ErrorCode;
import com.example.bmillion_backend.core.error.exception.DuplicateException;
import com.example.bmillion_backend.core.error.exception.NotFoundException;
import com.example.bmillion_backend.core.error.exception.UnAuthorizedException;
import com.example.bmillion_backend.core.security.JwtTokenProvider;
import com.example.bmillion_backend.dto.UserRequestDto;
import com.example.bmillion_backend.entity.UserEntity;
import com.example.bmillion_backend.entity.custom.UserRole;
import com.example.bmillion_backend.repo.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepo userRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRequestDto registerRequest, HttpServletResponse response) {
        if (userRepo.existsByUserId(registerRequest.getUserId()))
            throw new DuplicateException("Duplicated userId", ErrorCode.DUPLICATE_EXCEPTION);

        String AT = jwtTokenProvider.createAccessToken(registerRequest.getUserId(), UserRole.Normal);
        String RT = jwtTokenProvider.createRefreshToken(registerRequest.getUserId(), UserRole.Normal);

        UserEntity userEntity = UserEntity.builder()
                .userId(registerRequest.getUserId())
                .userRole(UserRole.Normal)
                .password(registerRequest.getPassword() != null ? passwordEncoder.encode(registerRequest.getPassword()) : null)
                .refreshToken(RT)
                .build();

        userRepo.save(userEntity);

        jwtTokenProvider.setHeaderAccessToken(response,AT);
        jwtTokenProvider.setHeaderRefreshToken(response,RT);
    }

    @Transactional
    public void normalLogin(UserRequestDto loginRequest, HttpServletResponse response) {
        UserEntity userEntity = userRepo.findByUserId(loginRequest.getUserId())
                .orElseThrow(()->new NotFoundException("Cannot find matched id", ErrorCode.NOT_FOUND_EXCEPTION));
        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword()))
            throw new UnAuthorizedException("This password is incorrect", ErrorCode.UNAUTHORIZED_EXCEPTION);

        String AT = jwtTokenProvider.createAccessToken(userEntity.getUserId(), userEntity.getUserRole());
        String RT = jwtTokenProvider.createRefreshToken(userEntity.getUserId(), userEntity.getUserRole());

        userEntity.updateRefreshToken(RT);

        jwtTokenProvider.setHeaderAccessToken(response,AT);
        jwtTokenProvider.setHeaderRefreshToken(response,RT);
    }

}
