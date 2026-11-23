package cl.duoc.sonidovivo.config;

import cl.duoc.sonidovivo.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;
import static org.springframework.security.config.Elements.JWT;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{"error":"No autenticado. Debes enviar un token JWT válido."}");
                })
                .accessDeniedHandler((req, res, accessEx) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{"error":"Acceso denegado. No tienes permisos suficientes para este recurso."}");
                })
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos: Auth y Swagger
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Catálogo público de lectura (o para usuarios autenticados)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").permitAll()

                // Gestión de productos: ADMINISTRADOR y VENDEDOR (stock/productos)
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMINISTRADOR")

                // Gestión de categorías: solo ADMINISTRADOR
                .requestMatchers("/api/v1/categorias/**").hasRole("ADMINISTRADOR")

                // Perfil propio y gestión de usuarios
                .requestMatchers("/api/v1/usuarios/me").authenticated()
                .requestMatchers("/api/v1/usuarios/**").hasRole("ADMINISTRADOR")

                // Pedidos / Boletas
                .requestMatchers(HttpMethod.POST, "/api/v1/boletas/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/boletas/mis-compras").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/boletas/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/boletas/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*", "*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
