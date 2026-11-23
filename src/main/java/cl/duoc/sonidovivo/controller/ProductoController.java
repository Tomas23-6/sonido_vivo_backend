package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.dto.ProductoRequest;
import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones CRUD del catálogo de instrumentos musicales")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos o filtra por categoría / búsqueda.")
    public ResponseEntity<List<Producto>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(productoService.listarTodos(categoria, search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por código")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear producto (Admin / Vendedor)")
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto completo (Admin / Vendedor)")
    public ResponseEntity<Producto> actualizar(@PathVariable String id, @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock de un producto (Admin / Vendedor)")
    public ResponseEntity<Producto> actualizarStock(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        Integer stock = body.get("stock");
        if (stock == null) {
            throw new IllegalArgumentException("Debes enviar el campo 'stock'.");
        }
        return ResponseEntity.ok(productoService.actualizarStock(id, stock));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (Solo Admin)")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
