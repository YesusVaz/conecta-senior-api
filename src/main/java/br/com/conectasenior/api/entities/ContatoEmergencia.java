package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade ContatoEmergencia - Representa contatos para situações de emergência
 *
 * Armazena informações de familiares, cuidadores ou responsáveis que devem
 * ser contatados em casos de emergência envolvendo o idoso.
 */
@Entity
@Table(name = "contatos_emergencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContatoEmergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do contato é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    @Column(nullable = false, length = 11)
    private String telefone;

    @NotBlank(message = "Relacionamento é obrigatório")
    @Size(max = 50, message = "Relacionamento deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String relacionamento; // Ex: Filho, Filha, Cuidador, Médico

    @Email(message = "Email deve ser válido")
    @Column(length = 100)
    private String email;

    @NotNull(message = "Prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade deve ser no mínimo 1")
    @Max(value = 10, message = "Prioridade deve ser no máximo 10")
    @Column(nullable = false)
    private Integer prioridade; // 1 = mais prioritário, 10 = menos prioritário

    @Size(max = 200, message = "Observações deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // Relacionamento com Idoso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idoso_id", nullable = false)
    private Idoso idoso;
}
