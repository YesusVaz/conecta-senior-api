package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.RegistroSaude;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para operações com Registro de Saúde
 */
@Data
public class RegistroSaudeDTO {

    private Long id;

    @NotNull(message = "Tipo de registro é obrigatório")
    private RegistroSaude.TipoRegistro tipo;

    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser positivo")
    @Digits(integer = 5, fraction = 2, message = "Valor deve ter no máximo 5 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unidade;

    @Size(max = 500, message = "Observações deve ter no máximo 500 caracteres")
    private String observacoes;

    @NotNull(message = "Data/hora do registro é obrigatória")
    private LocalDateTime dataRegistro;

    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;

    private LocalDateTime criadoEm;
}
