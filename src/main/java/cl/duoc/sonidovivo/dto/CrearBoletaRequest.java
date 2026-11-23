package cl.duoc.sonidovivo.dto;

import cl.duoc.sonidovivo.model.TipoEntrega;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CrearBoletaRequest {
    @NotNull(message = "El tipo de entrega es obligatorio (DESPACHO o RETIRO)")
    private TipoEntrega tipoEntrega;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    private String indicaciones;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<ItemBoletaRequest> items;

    @Data
    public static class ItemBoletaRequest {
        @NotBlank
        private String productoId;
        @NotNull
        private Integer cantidad;
    }
}
