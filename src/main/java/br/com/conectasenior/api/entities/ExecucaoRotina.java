package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade ExecucaoRotina - Registra quando uma rotina foi executada
 *
 * Permite rastrear o cumprimento das rotinas programadas, identificando
 * padrões de adesão e possíveis problemas no seguimento do tratamento.
 */
@Entity
@Table(name = "execucoes_rotina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecucaoRotina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data/hora de execução é obrigatória")
    @Column(name = "executado_em", nullable = false)
    private LocalDateTime executadoEm;

    @NotNull(message = "Status de execução é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusExecucao status;

    @Size(max = 300, message = "Observações deve ter no máximo 300 caracteres")
    @Column(length = 300)
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // Relacionamento com Rotina
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rotina_id", nullable = false)
    private Rotina rotina;

    /**
     * Enum para status de execução da rotina
     */
    public enum StatusExecucao {
        EXECUTADA("Executada"),
        ATRASADA("Executada com Atraso"),
        PULADA("Pulada"),
        PARCIALMENTE_EXECUTADA("Parcialmente Executada");

        private final String descricao;

        StatusExecucao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
