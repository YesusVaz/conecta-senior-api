package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.Rotina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

/**
 * Repositório para operações com a entidade Rotina
 */
@Repository
public interface RotinaRepository extends JpaRepository<Rotina, Long> {

    /**
     * Busca rotinas ativas de um idoso
     */
    List<Rotina> findByIdosoIdAndAtivoTrueOrderByHorarioAsc(Long idosoId);

    /**
     * Busca rotinas por tipo e idoso
     */
    List<Rotina> findByIdosoIdAndTipoAndAtivoTrue(Long idosoId, Rotina.TipoRotina tipo);

    /**
     * Busca rotinas por tipo específico (global)
     */
    List<Rotina> findByTipoAndAtivoTrueOrderByHorarioAsc(Rotina.TipoRotina tipo);

    /**
     * Busca rotinas em um intervalo de horário
     */
    @Query("SELECT r FROM Rotina r WHERE r.idoso.id = :idosoId AND r.horario BETWEEN :horarioInicio AND :horarioFim AND r.ativo = true")
    List<Rotina> findRotinasPorIntervaloHorario(@Param("idosoId") Long idosoId,
                                               @Param("horarioInicio") LocalTime horarioInicio,
                                               @Param("horarioFim") LocalTime horarioFim);

    /**
     * Busca rotinas que contêm um dia específico da semana
     */
    @Query("SELECT r FROM Rotina r WHERE r.idoso.id = :idosoId AND (r.diasSemana LIKE '%TODOS%' OR r.diasSemana LIKE %:diaSemana%) AND r.ativo = true")
    List<Rotina> findRotinasPorDiaSemana(@Param("idosoId") Long idosoId, @Param("diaSemana") String diaSemana);

    /**
     * Busca todas as rotinas ativas paginadas
     */
    Page<Rotina> findByAtivoTrue(Pageable pageable);

    /**
     * Conta rotinas ativas por idoso
     */
    Long countByIdosoIdAndAtivoTrue(Long idosoId);

    /**
     * Busca rotinas por horário específico
     */
    @Query("SELECT r FROM Rotina r WHERE r.horario = :horario AND r.ativo = true ORDER BY r.idoso.nome")
    List<Rotina> findRotinasNoHorario(@Param("horario") LocalTime horario);
}
