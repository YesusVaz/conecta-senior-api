package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.Idoso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Idoso
 *
 * Inclui consultas customizadas para busca por nome, CPF, idade
 * e relatórios específicos do domínio.
 */
@Repository
public interface IdosoRepository extends JpaRepository<Idoso, Long> {

    /**
     * Busca idoso por CPF (campo único)
     */
    Optional<Idoso> findByCpf(String cpf);

    /**
     * Busca idosos por nome (busca parcial e case-insensitive)
     */
    Page<Idoso> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    /**
     * Busca idosos por email
     */
    Optional<Idoso> findByEmail(String email);

    /**
     * Busca idosos nascidos entre determinadas datas (para filtro por idade)
     */
    @Query("SELECT i FROM Idoso i WHERE i.dataNascimento BETWEEN :dataInicio AND :dataFim")
    List<Idoso> findByIdadeEntre(@Param("dataInicio") LocalDate dataInicio,
                                 @Param("dataFim") LocalDate dataFim);

    /**
     * Busca idosos que possuem emergências ativas
     */
    @Query("SELECT DISTINCT i FROM Idoso i JOIN i.emergencias e WHERE e.status = 'ATIVA'")
    List<Idoso> findIdososComEmergenciasAtivas();

    /**
     * Conta idosos sem contatos de emergência cadastrados
     */
    @Query("SELECT i FROM Idoso i WHERE i.contatosEmergencia IS EMPTY")
    List<Idoso> findIdososSemContatosEmergencia();

    /**
     * Busca idosos por cidade (extraindo do endereço)
     */
    @Query("SELECT i FROM Idoso i WHERE LOWER(i.endereco) LIKE LOWER(CONCAT('%', :cidade, '%'))")
    List<Idoso> findByEnderecoContainingCidade(@Param("cidade") String cidade);
}
