package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.dto.UsuarioUpdateRequest;
import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + correo));
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioUpdateRequest req) {
        Usuario u = obtenerPorId(id);
        if (req.getNombre() != null) u.setNombre(req.getNombre().trim());
        if (req.getApellido() != null) u.setApellido(req.getApellido().trim());
        if (req.getFechaNacimiento() != null) u.setFechaNacimiento(req.getFechaNacimiento());
        if (req.getDireccion() != null) u.setDireccion(req.getDireccion().trim());
        if (req.getRegion() != null) u.setRegion(req.getRegion().trim());
        if (req.getGenero() != null) u.setGenero(req.getGenero());
        if (req.getRol() != null) u.setRol(req.getRol());
        if (req.getActivo() != null) u.setActivo(req.getActivo());
        if (req.getBloqueado() != null) {
            u.setBloqueado(req.getBloqueado());
            if (!req.getBloqueado()) {
                u.setIntentosFallidos(0);
            }
        }
        return usuarioRepository.save(u);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario u = obtenerPorId(id);
        usuarioRepository.delete(u);
    }
}
