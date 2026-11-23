package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.dto.UsuarioUpdateRequest;
import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de cuentas, roles y perfil del usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<Usuario> miPerfil(Authentication auth) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(auth.getName()));
    }

    @PutMapping("/me")
    @Operation(summary = "Actualizar perfil del usuario autenticado")
    public ResponseEntity<Usuario> actualizarMiPerfil(Authentication auth, @RequestBody UsuarioUpdateRequest req) {
        Usuario actual = usuarioService.obtenerPorCorreo(auth.getName());
        // Evitar que un usuario cambie su propio rol desde /me
        req.setRol(null);
        req.setActivo(null);
        req.setBloqueado(null);
        return ResponseEntity.ok(usuarioService.actualizarUsuario(actual.getId(), req));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios (Solo Admin)")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID (Solo Admin)")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar usuario, asignar rol o desbloquear/desactivar (Solo Admin)")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody UsuarioUpdateRequest req) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (Solo Admin)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
