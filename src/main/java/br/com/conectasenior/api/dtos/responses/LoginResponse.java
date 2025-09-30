package br.com.conectasenior.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
    private String email;
    private String nome;
    private String papel;
}
