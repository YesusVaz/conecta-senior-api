package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.ExecucaoRotina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações com a entidade ExecucaoRotina
 */
@Repository
public interface ExecucaoRotinaRepository extends JpaRepository<ExecucaoRotina, Long> {

    /**
     * Busca execuções de uma rotina específica
     */
    List<ExecucaoRotina> findByRotinaIdOrderByExecutadoEmDesc(Long rotinaId);

    /**
     * Busca execuções em um período específico
     */
    @Query("SELECT e FROM ExecucaoRotina e WHERE e.rotina.id = :rotinaId AND e.executadoEm BETWEEN :inicio AND :fim")
    List<ExecucaoRotina> findExecucoesPorPeriodo(@Param("rotinaId") Long rotinaId,
                                                @Param("inicio") LocalDateTime inicio,
                                                @Param("fim") LocalDateTime fim);

    /**
     * Busca execuções por status
     */
    List<ExecucaoRotina> findByStatus(ExecucaoRotina.StatusExecucao status);

    /**
     * Calcula taxa de adesão de uma rotina (execuções vs total esperado)
     */
    @Query("SELECT COUNT(e) FROM ExecucaoRotina e WHERE e.rotina.id = :rotinaId AND e.status = 'EXECUTADA'")
    Long countExecucoesRealizadas(@Param("rotinaId") Long rotinaId);
}
