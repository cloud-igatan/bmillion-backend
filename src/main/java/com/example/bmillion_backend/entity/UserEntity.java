package com.example.bmillion_backend.entity;

import com.example.bmillion_backend.entity.custom.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostEntity> posts;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String refreshToken;

    private UserRole userRole;

    public void updateRefreshToken(String RT) {
        this.refreshToken = RT;
    }

}
