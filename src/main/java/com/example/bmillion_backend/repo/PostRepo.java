package com.example.bmillion_backend.repo;

import com.example.bmillion_backend.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUserId(Long userId);
}
