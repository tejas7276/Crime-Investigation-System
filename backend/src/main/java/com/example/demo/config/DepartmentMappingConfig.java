package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DepartmentMappingConfig {

    @Bean
    @Primary
    @Qualifier("preferredDepartmentMapping")
    public Map<String, String> crimeTypeToPreferredDepartment() {
        Map<String, String> mapping = new HashMap<>();

        mapping.put("cyber crime", "Cyber Crime");
        mapping.put("cyber", "Cyber Crime");
        mapping.put("online fraud", "Cyber Crime");

        mapping.put("theft", "Theft");
        mapping.put("burglary", "Theft");
        mapping.put("shoplifting", "Theft");

        mapping.put("assault", "Violent Crimes");
        mapping.put("murder", "Violent Crimes");
        mapping.put("kidnapping", "Violent Crimes");
        mapping.put("domestic violence", "Violent Crimes");

        mapping.put("robbery", "Robbery");
        mapping.put("armed robbery", "Robbery");
        mapping.put("bank robbery", "Robbery");

        mapping.put("drug trafficking", "Narcotics");
        mapping.put("possession of drugs", "Narcotics");

        mapping.put("sexual harassment", "Women Cell");
        mapping.put("rape", "Women Cell");
        mapping.put("stalking", "Women Cell");

        mapping.put("vandalism", "Public Safety");
        mapping.put("traffic violation", "Traffic Department");
        mapping.put("hit and run", "Traffic Department");

        return mapping;
    }

    // Fallback department mapping
    @Bean
    public Map<String, String> crimeTypeToFallbackDepartment() {
        Map<String, String> fallbackMapping = new HashMap<>();

        fallbackMapping.put("cyber crime", "General Dept");
        fallbackMapping.put("cyber", "General Dept");
        fallbackMapping.put("online fraud", "General Dept");

        fallbackMapping.put("theft", "General Dept");
        fallbackMapping.put("burglary", "General Dept");
        fallbackMapping.put("shoplifting", "General Dept");

        fallbackMapping.put("assault", "Violent Crimes");
        fallbackMapping.put("murder", "Violent Crimes");
        fallbackMapping.put("kidnapping", "Violent Crimes");
        fallbackMapping.put("domestic violence", "Violent Crimes");

        fallbackMapping.put("robbery", "Senior Officer Dept");
        fallbackMapping.put("armed robbery", "Senior Officer Dept");
        fallbackMapping.put("bank robbery", "Senior Officer Dept");

        fallbackMapping.put("drug trafficking", "General Dept");
        fallbackMapping.put("possession of drugs", "General Dept");

        fallbackMapping.put("sexual harassment", "Women Cell");
        fallbackMapping.put("rape", "Women Cell");
        fallbackMapping.put("stalking", "Women Cell");

        fallbackMapping.put("vandalism", "Public Safety");
        fallbackMapping.put("traffic violation", "Traffic Department");
        fallbackMapping.put("hit and run", "Traffic Department");

        return fallbackMapping;
    }
}
