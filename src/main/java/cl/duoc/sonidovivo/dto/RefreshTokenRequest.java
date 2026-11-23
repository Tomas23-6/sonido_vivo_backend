package cl.duoc.sonidovivo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "El refreshToken es obligatorio")
    private String refreshToken;
}
