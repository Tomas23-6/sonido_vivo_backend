package cl.duoc.sonidovivo.repository;

import cl.duoc.sonidovivo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByCategoriaNombreIgnoreCase(String categoria);
    List<Producto> findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(
            String nombre, String marca, String modelo);
    long countByCategoriaId(Long categoriaId);
}
