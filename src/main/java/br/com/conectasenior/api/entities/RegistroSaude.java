package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade RegistroSaude - Armazena dados vitais e condições de saúde
 *
 * Centraliza informações como pressão arterial, glicemia, peso, medicamentos
 * e sintomas relatados pelo idoso ou cuidador.
 */
@Entity
@Table(name = "registros_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tipo de registro é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRegistro tipo;

    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser positivo")
    @Digits(integer = 5, fraction = 2, message = "Valor deve ter no máximo 5 dígitos inteiros e 2 decimais")
    @Column(precision = 7, scale = 2)
    private BigDecimal valor;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    @Column(length = 20)
    private String unidade; // Ex: mmHg, mg/dL, kg, °C

    @Size(max = 500, message = "Observações deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String observacoes;

    @NotNull(message = "Data/hora do registro é obrigatória")
    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // Relacionamento com Idoso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoso_id", nullable = false)
    private Idoso idoso;

    /**
     * Enum para tipos de registro de saúde
     */
    public enum TipoRegistro {
        PRESSAO_ARTERIAL("Pressão Arterial"),
        GLICEMIA("Glicemia"),
        PESO("Peso Corporal"),
        ALTURA("Altura"),
        TEMPERATURA("Temperatura Corporal"),
        FREQUENCIA_CARDIACA("Frequência Cardíaca"),
        SATURACAO_OXIGENIO("Saturação de Oxigênio"),
        MEDICAMENTO_TOMADO("Medicamento Administrado"),
        SINTOMA("Sintoma Relatado"),
        HUMOR("Estado de Humor"),
        DOR("Nível de Dor"),
        SONO("Qualidade do Sono"),
        OUTROS("Outros");

        private final String descricao;

        TipoRegistro(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
