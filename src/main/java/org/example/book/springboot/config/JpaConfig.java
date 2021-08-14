package org.example.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@WebMvcTest에서 스캔하지 않게 하기위해 Application.java에서 설정 분리.
// -> @WebMvcTest는 @SpringBootApplication는 스캔하지만, 일반적인 @Configuration은 스캔하지 않음.
@Configuration
@EnableJpaAuditing //JPA Auditing 활성화
public class JpaConfig { }
