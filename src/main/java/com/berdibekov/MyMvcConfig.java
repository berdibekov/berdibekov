package com.berdibekov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan
@EnableWebMvc
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

}