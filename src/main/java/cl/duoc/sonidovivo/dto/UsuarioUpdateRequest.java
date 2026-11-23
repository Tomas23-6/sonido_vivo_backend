package cl.duoc.sonidovivo.dto;

import cl.duoc.sonidovivo.model.Rol;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioUpdateRequest {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String region;
    private String genero;
    private Rol rol;
    private Boolean activo;
    private Boolean bloqueado;
}
