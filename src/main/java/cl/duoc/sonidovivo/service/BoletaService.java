package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.dto.CrearBoletaRequest;
import cl.duoc.sonidovivo.model.*;
import cl.duoc.sonidovivo.repository.BoletaRepository;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Boleta crearPedido(String correoUsuario, CrearBoletaRequest req) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        Boleta boleta = Boleta.builder()
                .numeroOrden("#2026" + ThreadLocalRandom.current().nextInt(1000, 9999))
                .fecha(LocalDateTime.now())
                .usuario(usuario)
                .tipoEntrega(req.getTipoEntrega())
                .direccionEntrega(req.getDireccionEntrega().trim())
                .region(req.getRegion().trim())
                .comuna(req.getComuna().trim())
                .indicaciones(req.getIndicaciones())
                .estado(EstadoBoleta.EN_PREPARACION)
                .total(0)
                .build();

        int total = 0;
        for (CrearBoletaRequest.ItemBoletaRequest itemReq : req.getItems()) {
            Producto prod = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + itemReq.getProductoId()));

            if (itemReq.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }
            if (prod.getStock() < itemReq.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + prod.getNombre() + ". Disponible: " + prod.getStock());
            }

            // Descuento automático y atómico de stock (evita sobreventa entre vendedores/clientes)
            prod.setStock(prod.getStock() - itemReq.getCantidad());
            productoRepository.save(prod);

            int precioUnit = prod.getPrecioOferta() != null ? prod.getPrecioOferta() : prod.getPrecio();
            int subtotal = precioUnit * itemReq.getCantidad();
            total += subtotal;

            DetalleBoleta detalle = DetalleBoleta.builder()
                    .boleta(boleta)
                    .producto(prod)
                    .cantidad(itemReq.getCantidad())
                    .precioUnitario(precioUnit)
                    .subtotal(subtotal)
                    .build();

            boleta.getDetalles().add(detalle);
        }

        boleta.setTotal(total);
        return boletaRepository.save(boleta);
    }

    public List<Boleta> listarMisCompras(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
        return boletaRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId());
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAllByOrderByFechaDesc();
    }

    @Transactional
    public Boleta actualizarEstado(Long idBoleta, EstadoBoleta nuevoEstado) {
        Boleta boleta = boletaRepository.findById(idBoleta)
                .orElseThrow(() -> new EntityNotFoundException("Boleta no encontrada con id: " + idBoleta));

        if (nuevoEstado == EstadoBoleta.RECHAZADO && boleta.getEstado() != EstadoBoleta.RECHAZADO) {
            for (DetalleBoleta detalle : boleta.getDetalles()) {
                Producto prod = detalle.getProducto();
                prod.setStock(prod.getStock() + detalle.getCantidad());
                productoRepository.save(prod);
            }
        }

        boleta.setEstado(nuevoEstado);
        return boletaRepository.save(boleta);
    }

}
