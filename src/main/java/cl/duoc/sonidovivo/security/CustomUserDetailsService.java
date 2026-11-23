package cl.duoc.sonidovivo.security;

import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return new User(
                u.getCorreo(),
                u.getPassword(),
                Boolean.TRUE.equals(u.getActivo()),
                true,
                true,
                !Boolean.TRUE.equals(u.getBloqueado()),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().name()))
        );
    }
}
