package com.asi.hms.config;

import com.asi.hms.utils.cloudproviderutils.enums.RegionInterface;
import com.asi.hms.utils.cloudproviderutils.enums.RuntimeInterface;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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

        return mapper;

    }

}
