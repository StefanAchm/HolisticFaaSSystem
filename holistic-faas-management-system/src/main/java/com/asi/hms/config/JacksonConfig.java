package com.asi.hms.config;

import com.asi.hms.enums.RegionInterface;
import com.asi.hms.enums.RuntimeInterface;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        // Ignore unknown properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addSerializer(RegionInterface.class, new RegionInterface.RegionSerializer());
        module.addSerializer(RuntimeInterface.class, new RuntimeInterface.RuntimeSerializer());
        mapper.registerModule(module);

        mapper.registerModule(new JavaTimeModule());

        return mapper;

    }

}
