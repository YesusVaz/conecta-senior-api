package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.Rotina;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para operações com Rotina
 */
@Data
public class RotinaDTO {

    private Long id;

    @NotBlank(message = "Nome da rotina é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "Tipo da rotina é obrigatório")
    private Rotina.TipoRotina tipo;

    @NotNull(message = "Horário é obrigatório")
    private LocalDateTime horario;

    private Boolean recorrente;
    private Boolean ativa;

    @Size(max = 300, message = "Observações deve ter no máximo 300 caracteres")
    private String observacoes;

    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;

    private LocalDateTime dataCriacao;
}
