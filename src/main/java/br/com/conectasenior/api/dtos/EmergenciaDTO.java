package br.com.conectasenior.api.dtos;

import br.com.conectasenior.api.entities.Emergencia;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTOs para operações com Emergência
 */
@Data
public class EmergenciaDTO {

    private Long id;

    @NotNull(message = "Tipo de emergência é obrigatório")
    private Emergencia.TipoEmergencia tipo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    private String descricao;

    @NotNull(message = "Nível de gravidade é obrigatório")
    private Emergencia.NivelGravidade gravidade;

    private Emergencia.StatusEmergencia status;

    @Size(max = 100, message = "Localização deve ter no máximo 100 caracteres")
    private String localizacao;

    @Size(max = 100, message = "Contato acionado deve ter no máximo 100 caracteres")
    private String contatoAcionado;

    private LocalDateTime resolvidoEm;

    @Size(max = 500, message = "Resolução deve ter no máximo 500 caracteres")
    private String resolucao;

    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

/**
 * DTO para criação de Emergência
 */
@Data
class EmergenciaCreateDTO {

    @NotNull(message = "Tipo de emergência é obrigatório")
    private Emergencia.TipoEmergencia tipo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    private String descricao;

    @NotNull(message = "Nível de gravidade é obrigatório")
    private Emergencia.NivelGravidade gravidade;

    @Size(max = 100, message = "Localização deve ter no máximo 100 caracteres")
    private String localizacao;

    @NotNull(message = "ID do idoso é obrigatório")
    private Long idosoId;
}
