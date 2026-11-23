package cl.duoc.sonidovivo.dto;

import cl.duoc.sonidovivo.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private Usuario usuario;
}
