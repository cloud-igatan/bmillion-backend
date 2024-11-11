package com.example.bmillion_backend.repo;

import com.example.bmillion_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);

}
