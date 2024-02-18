package com.dailyReport.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 생성자 자동 생성
@Log4j2
public class SecurityConfig {

    private final EntityManager em; // 데이터베이스 연산 처리

    @Bean // Querydsl 사용 시 JPA 쿼리를 생성하는 팩토리 클래스
    // 복잡한 쿼리 작업을 더 간결하고 유연하게 구현 가능
    public JPAQueryFactory queryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin(login ->
                        login
                                .loginPage("/members/login")
                                .defaultSuccessUrl("/", true)
                                .usernameParameter("email")
                                .failureUrl("/members/login/error")
                )
                .logout(logoutConfig -> logoutConfig
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")).logoutSuccessUrl("/")
                );

        http.
                authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}