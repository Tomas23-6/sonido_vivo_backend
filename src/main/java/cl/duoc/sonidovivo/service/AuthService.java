package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.dto.*;
import cl.duoc.sonidovivo.model.Rol;
import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import cl.duoc.sonidovivo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public Usuario registrar(RegisterRequest req) {
        if (Period.between(req.getFechaNacimiento(), LocalDate.now()).getYears() < 14) {
            throw new IllegalArgumentException("Debes tener al menos 14 años para registrarte.");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(req.getCorreo().trim())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado.");
        }

        Usuario nuevo = Usuario.builder()
                .nombre(req.getNombre().trim())
                .apellido(req.getApellido().trim())
                .fechaNacimiento(req.getFechaNacimiento())
                .correo(req.getCorreo().trim().toLowerCase())
                .password(passwordEncoder.encode(req.getPassword()))
                .direccion(req.getDireccion().trim())
                .region(req.getRegion().trim())
                .genero(req.getGenero() != null ? req.getGenero() : "Otro")
                .rol(Rol.CLIENTE)
                .activo(true)
                .intentosFallidos(0)
                .bloqueado(false)
                .build();

        return usuarioRepository.save(nuevo);
    }

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public AuthResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(req.getCorreo().trim())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas o usuario no registrado."));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalStateException("Esta cuenta se encuentra desactivada. Contacta al administrador.");
        }
        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalStateException("Esta cuenta está BLOQUEADA tras 3 intentos fallidos.");
        }

        if (!passwordEncoder.matches(req.getPassword(), usuario.getPassword())) {
            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);
            if (intentos >= 3) {
                usuario.setBloqueado(true);
                usuarioRepository.save(usuario);
                throw new IllegalStateException("Has alcanzado el límite de 3 intentos. Tu cuenta fue BLOQUEADA.");
            }
            usuarioRepository.save(usuario);
            throw new IllegalArgumentException("Contraseña incorrecta. Intentos restantes: " + (3 - intentos));
        }

        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(usuario))
                .refreshToken(jwtService.generateRefreshToken(usuario))
                .tokenType("Bearer")
                .expiresIn(jwtService.getJwtExpirationMs() / 1000)
                .usuario(usuario)
                .build();
    }
    public AuthResponse refreshToken(String refreshToken) {
        try {
            String correo = jwtService.extractUsername(refreshToken);
            Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

            if (!Boolean.TRUE.equals(usuario.getActivo()) || Boolean.TRUE.equals(usuario.getBloqueado())) {
                throw new IllegalStateException("La cuenta no está habilitada.");
            }

            return AuthResponse.builder()
                    .accessToken(jwtService.generateAccessToken(usuario))
                    .refreshToken(jwtService.generateRefreshToken(usuario))
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getJwtExpirationMs() / 1000)
                    .usuario(usuario)
                    .build();
        } catch (io.jsonwebtoken.JwtException e) {
            throw new IllegalArgumentException("El refresh token es inválido o expiró. Inicia sesión nuevamente.");
        }
    }
}
