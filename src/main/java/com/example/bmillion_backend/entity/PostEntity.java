package com.example.bmillion_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PostEntity extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content", nullable = false)
    private String content;

}
