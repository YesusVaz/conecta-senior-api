package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.ContatoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações com a entidade ContatoEmergencia
 */
@Repository
public interface ContatoEmergenciaRepository extends JpaRepository<ContatoEmergencia, Long> {

    /**
     * Busca contatos de emergência por idoso, ordenados por prioridade
     */
    List<ContatoEmergencia> findByIdosoIdOrderByPrioridadeAsc(Long idosoId);

    /**
     * Busca contatos por telefone
     */
    List<ContatoEmergencia> findByTelefone(String telefone);

    /**
     * Busca contatos por tipo de relacionamento
     */
    List<ContatoEmergencia> findByRelacionamentoIgnoreCase(String relacionamento);

    /**
     * Busca contato principal (prioridade 1) de um idoso
     */
    @Query("SELECT c FROM ContatoEmergencia c WHERE c.idoso.id = :idosoId AND c.prioridade = 1")
    ContatoEmergencia findContatoPrincipalByIdosoId(@Param("idosoId") Long idosoId);
}
