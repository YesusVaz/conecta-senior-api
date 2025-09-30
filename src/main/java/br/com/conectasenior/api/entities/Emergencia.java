package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade Emergencia - Registra situações de emergência do idoso
 *
 * Armazena ocorrências críticas que requerem atenção imediata,
 * incluindo quedas, mal-estar, emergências médicas e acionamentos do botão de pânico.
 */
@Entity
@Table(name = "emergencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tipo de emergência é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEmergencia tipo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descricao;

    @NotNull(message = "Nível de gravidade é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private NivelGravidade gravidade;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusEmergencia status;

    @Size(max = 100, message = "Localização deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String localizacao;

    @Size(max = 100, message = "Contato acionado deve ter no máximo 100 caracteres")
    @Column(name = "contato_acionado", length = 100)
    private String contatoAcionado;

    @Column(name = "resolvido_em")
    private LocalDateTime resolvidoEm;

    @Size(max = 500, message = "Resolução deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String resolucao;

    // Campos de auditoria
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Relacionamento com Idoso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoso_id", nullable = false)
    private Idoso idoso;

    /**
     * Enum para tipos de emergência
     */
    public enum TipoEmergencia {
        QUEDA("Queda"),
        MAL_ESTAR("Mal-estar"),
        BOTAO_PANICO("Botão de Pânico"),
        EMERGENCIA_MEDICA("Emergência Médica"),
        ACIDENTE_DOMESTICO("Acidente Doméstico"),
        DESORIENTACAO("Desorientação"),
        OUTROS("Outros");

        private final String descricao;

        TipoEmergencia(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * Enum para níveis de gravidade
     */
    public enum NivelGravidade {
        BAIXA("Baixa"),
        MEDIA("Média"),
        ALTA("Alta"),
        CRITICA("Crítica");

        private final String descricao;

        NivelGravidade(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * Enum para status da emergência
     */
    public enum StatusEmergencia {
        ATIVA("Ativa"),
        EM_ATENDIMENTO("Em Atendimento"),
        RESOLVIDA("Resolvida"),
        CANCELADA("Cancelada");

        private final String descricao;

        StatusEmergencia(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
