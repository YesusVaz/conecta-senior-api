package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.RegistroSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade RegistroSaude
 */
@Repository
public interface RegistroSaudeRepository extends JpaRepository<RegistroSaude, Long> {

    /**
     * Busca registros de saúde de um idoso, ordenados por data
     */
    Page<RegistroSaude> findByIdosoIdOrderByRegistradoEmDesc(Long idosoId, Pageable pageable);

    /**
     * Busca registros por tipo específico
     */
    List<RegistroSaude> findByIdosoIdAndTipoOrderByRegistradoEmDesc(Long idosoId, RegistroSaude.TipoRegistro tipo);

    /**
     * Busca registros em um período específico
     */
    @Query("SELECT r FROM RegistroSaude r WHERE r.idoso.id = :idosoId AND r.registradoEm BETWEEN :inicio AND :fim ORDER BY r.registradoEm DESC")
    List<RegistroSaude> findRegistrosPorPeriodo(@Param("idosoId") Long idosoId,
                                               @Param("inicio") LocalDateTime inicio,
                                               @Param("fim") LocalDateTime fim);

    /**
     * Busca último registro de um tipo específico
     */
    @Query("SELECT r FROM RegistroSaude r WHERE r.idoso.id = :idosoId AND r.tipo = :tipo ORDER BY r.registradoEm DESC LIMIT 1")
    Optional<RegistroSaude> findUltimoRegistroPorTipo(@Param("idosoId") Long idosoId, @Param("tipo") RegistroSaude.TipoRegistro tipo);

    /**
     * Busca registros com valores fora da faixa normal (para alertas)
     */
    @Query("SELECT r FROM RegistroSaude r WHERE r.idoso.id = :idosoId AND r.tipo = :tipo AND (r.valor < :minimo OR r.valor > :maximo)")
    List<RegistroSaude> findRegistrosForaDaFaixa(@Param("idosoId") Long idosoId,
                                                @Param("tipo") RegistroSaude.TipoRegistro tipo,
                                                @Param("minimo") Double minimo,
                                                @Param("maximo") Double maximo);

    /**
     * Busca registros por tipo com paginação
     */
    Page<RegistroSaude> findByTipoOrderByRegistradoEmDesc(RegistroSaude.TipoRegistro tipo, Pageable pageable);

    /**
     * Busca registros recentes (últimas 24h) de um idoso
     */
    @Query("SELECT r FROM RegistroSaude r WHERE r.idoso.id = :idosoId AND r.registradoEm >= :dataLimite ORDER BY r.registradoEm DESC")
    List<RegistroSaude> findRegistrosRecentes(@Param("idosoId") Long idosoId, @Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Conta registros por tipo de um idoso
     */
    Long countByIdosoIdAndTipo(Long idosoId, RegistroSaude.TipoRegistro tipo);

    /**
     * Busca média de valores por tipo em um período
     */
    @Query("SELECT AVG(r.valor) FROM RegistroSaude r WHERE r.idoso.id = :idosoId AND r.tipo = :tipo AND r.registradoEm BETWEEN :inicio AND :fim")
    Double findMediaValoresPorTipoEPeriodo(@Param("idosoId") Long idosoId,
                                          @Param("tipo") RegistroSaude.TipoRegistro tipo,
                                          @Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim);
}
