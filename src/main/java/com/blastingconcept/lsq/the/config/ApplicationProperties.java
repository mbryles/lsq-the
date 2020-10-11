package com.blastingconcept.lsq.the.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties("lsq.the")
@Data
@NoArgsConstructor
public class ApplicationProperties {
    
    private Api api;

    @Data
    @NoArgsConstructor
    public static class Api {

        private String uploadDirectory;

    }

}
