package com.first.firstmicroservice;

import com.first.firstmicroservice.filter.Log4jFilter;
import com.first.firstmicroservice.filter.XSSFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirstMicroServiceGeneralConfig {

    @Bean
    public Log4jFilter log4jFilter(){
        return new Log4jFilter();
    }

    @Bean
    public XSSFilter xssFilter(){
        return new XSSFilter();
    }
}
