package ru.urfu.voiceassistant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Конфигурационный класс для установки параметров безопасности в приложении.
 * Этот класс предоставляет конфигурации безопасности с использованием Spring Security
 * для управления доступом к различным конечным точкам и управления аутентификацией пользователей.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Конструктор класса {@link SecurityConfig} с использованием указанного UserDetailsService.
     *
     * @param userDetailsService служба для загрузки данных, специфичных для пользователя.
     */
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Создает bean {@link PasswordEncoder} с использованием {@link BCryptPasswordEncoder}.
     *
     * @return экземпляр {@link BCryptPasswordEncoder}.
     */
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает цепочку фильтров безопасности с использованием {@link HttpSecurity}.
     *
     * @param http {@link HttpSecurity}, предоставляющий настройки безопасности.
     * @return цепочка фильтров безопасности.
     * @throws Exception возникает в случае ошибок при настройке цепочки фильтров безопасности.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/activate/**").permitAll()
                                .requestMatchers("/password_reset/**").permitAll()
                                .requestMatchers("/password_recovery/**").permitAll()
                                .requestMatchers("/personal_page/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/download_file/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/upload_file/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/record/**").hasAnyRole("ADMIN", "USER")
                ).formLogin(
                        form -> form
                                .usernameParameter("email")
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/record")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    /**
     * Конфигурирует глобальный {@link AuthenticationManagerBuilder}
     * с использованием {@link UserDetailsService} и {@link PasswordEncoder}.
     *
     * @param auth {@link AuthenticationManagerBuilder} для настройки глобальной аутентификации.
     * @throws Exception возникает в случае ошибок при настройке глобальной аутентификации.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
