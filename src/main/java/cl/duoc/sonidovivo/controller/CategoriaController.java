package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.model.Categoria;
import cl.duoc.sonidovivo.repository.CategoriaRepository;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de familias de instrumentos musicales")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar categorías")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Crear categoría (Solo Admin)")
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre().trim())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        categoria.setNombre(categoria.getNombre().trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría (Solo Admin)")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria req) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        cat.setNombre(req.getNombre().trim());
        cat.setDescripcion(req.getDescripcion());
        return ResponseEntity.ok(categoriaRepository.save(cat));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría (Solo Admin)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        long asociados = productoRepository.countByCategoriaId(id);
        if (asociados > 0) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene " + asociados + " producto(s) asociado(s).");
        }
        categoriaRepository.delete(cat);
        return ResponseEntity.noContent().build();
    }
}
