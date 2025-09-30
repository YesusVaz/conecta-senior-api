package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.Rotina;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para criação de Rotinas
 */
@Data
@Schema(description = "Dados para criação de rotina")
public class RotinaCreateDTO {

    @Schema(description = "Nome da rotina", example = "Tomar medicamento")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Descrição detalhada da rotina", example = "Tomar medicamento X após o almoço")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Schema(description = "Horário da rotina", example = "2024-01-01T14:00:00")
    @NotNull(message = "Horário é obrigatório")
    private LocalDateTime horario;

    @Schema(description = "Tipo da rotina", example = "MEDICACAO")
    @NotNull(message = "Tipo é obrigatório")
    private Rotina.TipoRotina tipo;

    @Schema(description = "Se a rotina é recorrente", example = "true")
    private boolean recorrente = false;

    @Schema(description = "Se a rotina está ativa", example = "true")
    private boolean ativa = true;

    @Schema(description = "ID do idoso", example = "1")
    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;

    @Schema(description = "Observações adicionais", example = "Observar reações adversas")
    @Size(max = 300, message = "Observações devem ter no máximo 300 caracteres")
    private String observacoes;
}
