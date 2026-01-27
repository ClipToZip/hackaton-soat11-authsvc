package com.clicktozip.authsvc.adapter.out.mapper;

import com.clicktozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.clicktozip.authsvc.domain.model.User;

import java.util.UUID;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getUserId().toString(),
                entity.getName(),
                entity.getEmail(),
                entity.getPasswordHash()
        );
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setUserId(domain.getUserId() != null ? UUID.fromString(domain.getUserId()) : null);
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPassswordHash());
        return entity;
    }
}
