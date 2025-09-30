package br.com.conectasenior.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String tipo;
    private Long expiresIn;
}
