package github.projectgroup.receptoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // виключаємо CSRF
                .csrf(csrf -> csrf.disable())

                // дозволяємо всі запити без аутентифікації
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // виключаємо Basic Auth і Form Login
                .httpBasic(Customizer.withDefaults())  // залишаємо стандартну заготовку…
                .formLogin(Customizer.withDefaults());

        // і відключаємо їхню обробку
        http.httpBasic(basic -> basic.disable());
        http.formLogin(form -> form.disable());

        return http.build();
    }
}
