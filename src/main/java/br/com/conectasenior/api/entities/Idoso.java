package br.com.conectasenior.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Idoso - Representa o usuário principal do sistema
 *
 * Centraliza informações pessoais, contatos de emergência e histórico.
 * Esta é a entidade principal do domínio, conectada a rotinas, saúde e emergências.
 */
@Entity
@Table(name = "idosos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Idoso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Email(message = "Email deve ser válido")
    @Column(length = 100)
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
    @Column(length = 11)
    private String telefone;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String endereco;

    @Size(max = 500, message = "Observações deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String observacoes;

    // Campos de auditoria
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Relacionamentos
    @OneToMany(mappedBy = "idoso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContatoEmergencia> contatosEmergencia = new ArrayList<>();

    @OneToMany(mappedBy = "idoso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroSaude> registrosSaude = new ArrayList<>();

    @OneToMany(mappedBy = "idoso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rotina> rotinas = new ArrayList<>();

    @OneToMany(mappedBy = "idoso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emergencia> emergencias = new ArrayList<>();

    // Método utilitário para calcular idade
    public int getIdade() {
        return LocalDate.now().getYear() - this.dataNascimento.getYear();
    }
}
