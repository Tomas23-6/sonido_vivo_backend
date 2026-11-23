package cl.duoc.sonidovivo.service;

import cl.duoc.sonidovivo.dto.ProductoRequest;
import cl.duoc.sonidovivo.model.Categoria;
import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.repository.CategoriaRepository;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Producto> listarTodos(String categoria, String search) {
        if (search != null && !search.isBlank()) {
            String q = search.trim();
            return productoRepository
                    .findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(q, q, q);
        }
        if (categoria != null && !categoria.isBlank() && !"todas".equalsIgnoreCase(categoria)) {
            return productoRepository.findByCategoriaNombreIgnoreCase(categoria.trim());
        }
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(String id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con código: " + id));
    }

    @Transactional
    public Producto crear(ProductoRequest req) {
        Categoria cat = categoriaRepository.findByNombreIgnoreCase(req.getCategoria().trim())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + req.getCategoria()));

        String codigo = (req.getId() != null && !req.getId().isBlank())
                ? req.getId().trim().toUpperCase()
                : "PROD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        if (productoRepository.existsById(codigo)) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + codigo);
        }

        if (req.getPrecioOferta() != null && req.getPrecioOferta() >= req.getPrecio()) {
            throw new IllegalArgumentException("El precio de oferta debe ser menor al precio normal.");
        }

        Producto p = Producto.builder()
                .id(codigo)
                .nombre(req.getNombre().trim())
                .marca(req.getMarca().trim())
                .modelo(req.getModelo().trim())
                .categoria(cat)
                .precio(req.getPrecio())
                .precioOferta(req.getPrecioOferta())
                .stock(req.getStock())
                .imagen(req.getImagen() != null && !req.getImagen().isBlank()
                        ? req.getImagen().trim()
                        : "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600")
                .descripcion(req.getDescripcion().trim())
                .build();

        return productoRepository.save(p);
    }

    @Transactional
    public Producto actualizar(String id, ProductoRequest req) {
        Producto existente = obtenerPorId(id);
        Categoria cat = categoriaRepository.findByNombreIgnoreCase(req.getCategoria().trim())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + req.getCategoria()));

        if (req.getPrecioOferta() != null && req.getPrecioOferta() >= req.getPrecio()) {
            throw new IllegalArgumentException("El precio de oferta debe ser menor al precio normal.");
        }

        existente.setNombre(req.getNombre().trim());
        existente.setMarca(req.getMarca().trim());
        existente.setModelo(req.getModelo().trim());
        existente.setCategoria(cat);
        existente.setPrecio(req.getPrecio());
        existente.setPrecioOferta(req.getPrecioOferta());
        existente.setStock(req.getStock());
        if (req.getImagen() != null && !req.getImagen().isBlank()) {
            existente.setImagen(req.getImagen().trim());
        }
        existente.setDescripcion(req.getDescripcion().trim());

        return productoRepository.save(existente);
    }

    @Transactional
    public Producto actualizarStock(String id, int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        Producto existente = obtenerPorId(id);
        existente.setStock(nuevoStock);
        return productoRepository.save(existente);
    }

    @Transactional
    public void eliminar(String id) {
        Producto existente = obtenerPorId(id);
        productoRepository.delete(existente);
    }
}
