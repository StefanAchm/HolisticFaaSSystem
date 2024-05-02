package com.asi.hms.config.db;

import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    @Bean
    public AuditorAware<DBUser> auditorProvider() {

        return () -> {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {

                return Optional.empty();

            }

            return Optional.of( ((UserPrincipal) authentication.getPrincipal()).getDbUser());

        };

    }

}