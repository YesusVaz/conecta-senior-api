package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.Usuario;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTOs para operações com Usuario
 */
@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotNull(message = "Tipo de usuário é obrigatório")
    private Usuario.TipoUsuario tipo;

    private Boolean ativo;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

/**
 * DTO para criação de Usuario
 */
@Data
class UsuarioCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String senha;

    @NotNull(message = "Tipo de usuário é obrigatório")
    private Usuario.TipoUsuario tipo;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    private String telefone;
}

/**
 * DTO para login
 */
@Data
class LoginDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}

/**
 * DTO para resposta do login
 */
@Data
class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private UsuarioDTO usuario;
}
