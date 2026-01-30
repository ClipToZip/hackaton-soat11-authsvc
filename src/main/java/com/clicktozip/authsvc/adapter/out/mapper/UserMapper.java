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
                null
        );
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
            entity.setUserId(UUID.fromString(user.getUserId()));
        }
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPassswordHash());
        return entity;
    }
}
