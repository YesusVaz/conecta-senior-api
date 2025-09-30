package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.Emergencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações com a entidade Emergencia
 */
@Repository
public interface EmergenciaRepository extends JpaRepository<Emergencia, Long> {

    /**
     * Busca emergências ativas
     */
    List<Emergencia> findByStatusOrderByCriadoEmDesc(Emergencia.StatusEmergencia status);

    /**
     * Busca emergências de um idoso específico
     */
    Page<Emergencia> findByIdosoIdOrderByCriadoEmDesc(Long idosoId, Pageable pageable);

    /**
     * Busca emergências por nível de gravidade
     */
    List<Emergencia> findByGravidadeOrderByCriadoEmDesc(Emergencia.NivelGravidade gravidade);

    /**
     * Busca emergências em um período específico
     */
    @Query("SELECT e FROM Emergencia e WHERE e.criadoEm BETWEEN :inicio AND :fim ORDER BY e.criadoEm DESC")
    List<Emergencia> findEmergenciasPorPeriodo(@Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);

    /**
     * Conta emergências ativas por gravidade
     */
    @Query("SELECT COUNT(e) FROM Emergencia e WHERE e.status = 'ATIVA' AND e.gravidade = :gravidade")
    Long countEmergenciasAtivasPorGravidade(@Param("gravidade") Emergencia.NivelGravidade gravidade);

    /**
     * Busca emergências não resolvidas há mais de X horas
     */
    @Query("SELECT e FROM Emergencia e WHERE e.status IN ('ATIVA', 'EM_ATENDIMENTO') AND e.criadoEm < :tempoLimite")
    List<Emergencia> findEmergenciasNaoResolvidasApos(@Param("tempoLimite") LocalDateTime tempoLimite);
}
