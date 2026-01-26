package alvino.dev.challenge_forohub.config;

import alvino.dev.challenge_forohub.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
// @Profile("prod")
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Desactiva CSRF para API REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless con JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()  // Login abierto
                        .requestMatchers("/usuarios/**").permitAll()  // Temporal: registro usuarios abierto; cierra en prod si quieres
                        .requestMatchers("/perfiles/**").hasRole("ADMIN")  // Ejemplo: solo admin maneja perfiles
                        .requestMatchers(HttpMethod.POST, "/topicos", "/respuestas").authenticated()  // Crear requiere login
                        .requestMatchers(HttpMethod.PUT, "/topicos/**", "/respuestas/**").authenticated()  // Editar requiere login
                        .requestMatchers(HttpMethod.DELETE, "/topicos/**", "/respuestas/**").hasRole("MODERADOR")  // Eliminar requiere rol moderador
                        .anyRequest().permitAll()  // Lectura abierta (list, show)
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  // Filtro JWT antes de auth básica
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
