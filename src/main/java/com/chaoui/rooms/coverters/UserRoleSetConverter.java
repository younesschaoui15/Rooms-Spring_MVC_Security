package com.chaoui.rooms.coverters;

import com.chaoui.rooms.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
@Slf4j(topic = "UserRoleSetConverter")
public class UserRoleSetConverter implements AttributeConverter<Set<UserRole>, String> {

    @Override
    public String convertToDatabaseColumn(Set<UserRole> roles) {
        if (roles == null || roles.isEmpty())
            return "";

        return roles.stream()
            .map(Enum::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(String roles) {
        if (roles == null || roles.isBlank())
            return Collections.emptySet();

        return Arrays.stream(roles.split(","))
            .map(String::trim)
            .map(String::toUpperCase)
            .map(role -> {
                try {
                    return UserRole.valueOf(role);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid User Role: {}", role);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
}
