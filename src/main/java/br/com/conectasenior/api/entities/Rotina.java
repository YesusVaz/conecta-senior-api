package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Rotina - Representa atividades programadas do idoso
 *
 * Centraliza o agendamento de medicamentos, exercícios, consultas e outras
 * atividades importantes para o bem-estar do idoso.
 */
@Entity
@Table(name = "rotinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rotina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título da rotina é obrigatório")
    @Size(min = 3, max = 100, message = "Título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Tipo da rotina é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRotina tipo;

    @NotNull(message = "Horário é obrigatório")
    @Column(nullable = false)
    private LocalTime horario;

    @Size(max = 200, message = "Dias da semana deve ter no máximo 200 caracteres")
    @Column(name = "dias_semana", length = 200)
    private String diasSemana; // Ex: "SEG,TER,QUA" ou "TODOS"

    @NotNull(message = "Status ativo é obrigatório")
    @Column(nullable = false)
    private Boolean ativo = true;

    @Size(max = 300, message = "Observações deve ter no máximo 300 caracteres")
    @Column(length = 300)
    private String observacoes;

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

    // Relacionamento com execuções da rotina
    @OneToMany(mappedBy = "rotina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExecucaoRotina> execucoes = new ArrayList<>();

    /**
     * Enum para tipos de rotina
     */
    public enum TipoRotina {
        MEDICAMENTO("Medicamento"),
        EXERCICIO("Exercício"),
        CONSULTA("Consulta Médica"),
        ALIMENTACAO("Alimentação"),
        HIGIENE("Higiene Pessoal"),
        LAZER("Atividade de Lazer"),
        OUTROS("Outros");

        private final String descricao;

        TipoRotina(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
