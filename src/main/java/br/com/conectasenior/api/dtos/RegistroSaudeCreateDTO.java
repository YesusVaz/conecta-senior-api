package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.RegistroSaude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO para criação de Registros de Saúde
 */
@Data
@Schema(description = "Dados para criação de registro de saúde")
public class RegistroSaudeCreateDTO {

    @Schema(description = "Tipo do registro de saúde", example = "PRESSAO_ARTERIAL")
    @NotNull(message = "Tipo é obrigatório")
    private RegistroSaude.TipoRegistro tipo;

    @Schema(description = "Valor do registro", example = "120.5")
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @Schema(description = "Unidade de medida", example = "mmHg")
    @NotBlank(message = "Unidade é obrigatória")
    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unidade;

    @Schema(description = "ID do idoso", example = "1")
    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;

    @Schema(description = "Observações adicionais", example = "Medição realizada em jejum")
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;
}
