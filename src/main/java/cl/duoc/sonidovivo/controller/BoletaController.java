package cl.duoc.sonidovivo.controller;

import cl.duoc.sonidovivo.dto.CrearBoletaRequest;
import cl.duoc.sonidovivo.model.Boleta;
import cl.duoc.sonidovivo.model.EstadoBoleta;
import cl.duoc.sonidovivo.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
@Tag(name = "Boletas y Pedidos", description = "Flujo de compra, descuento de stock y seguimiento de estado")
public class BoletaController {

    private final BoletaService boletaService;

    @PostMapping
    @Operation(summary = "Confirmar pedido / generar boleta", description = "Descuenta stock y crea la boleta en estado EN_PREPARACION.")
    public ResponseEntity<Boleta> crearPedido(Authentication auth, @Valid @RequestBody CrearBoletaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.crearPedido(auth.getName(), request));
    }

    @GetMapping("/mis-compras")
    @Operation(summary = "Historial de compras del cliente autenticado")
    public ResponseEntity<List<Boleta>> misCompras(Authentication auth) {
        return ResponseEntity.ok(boletaService.listarMisCompras(auth.getName()));
    }

    @GetMapping
    @Operation(summary = "Listar todas las órdenes (Admin / Vendedor)")
    public ResponseEntity<List<Boleta>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido (Admin / Vendedor)")
    public ResponseEntity<Boleta> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estadoStr = body.get("estado");
        if (estadoStr == null) {
            throw new IllegalArgumentException("Debes indicar el nuevo 'estado' (EN_PREPARACION, DESPACHADO, ENTREGADO, RECHAZADO).");
        }
        EstadoBoleta nuevoEstado = EstadoBoleta.valueOf(estadoStr.trim().toUpperCase());
        return ResponseEntity.ok(boletaService.actualizarEstado(id, nuevoEstado));
    }
}
