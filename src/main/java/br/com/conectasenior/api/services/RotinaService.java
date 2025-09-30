package br.com.conectasenior.api.services;

import br.com.conectasenior.api.dtos.RotinaCreateDTO;
import br.com.conectasenior.api.dtos.RotinaDTO;
import br.com.conectasenior.api.entities.Rotina;
import br.com.conectasenior.api.exceptions.ResourceNotFoundException;
import br.com.conectasenior.api.repositories.RotinaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

/**
 * Service para operações com Rotinas
 *
 * Gerencia CRUD e operações específicas do domínio de rotinas,
 * seguindo as melhores práticas do Spring Boot.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RotinaService {

    private final RotinaRepository rotinaRepository;
    private final ModelMapper modelMapper;

    public Page<RotinaDTO> findAll(Pageable pageable) {
        log.debug("Buscando todas as rotinas - Página: {}", pageable.getPageNumber());
        return rotinaRepository.findByAtivoTrue(pageable)
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class));
    }

    public RotinaDTO findById(Long id) {
        log.debug("Buscando rotina por ID: {}", id);
        Rotina rotina = rotinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rotina não encontrada com ID: " + id));
        return modelMapper.map(rotina, RotinaDTO.class);
    }

    public List<RotinaDTO> findByIdoso(Long idosoId) {
        log.debug("Buscando rotinas do idoso ID: {}", idosoId);
        // OTIMIZAÇÃO: Usando consulta específica do banco ao invés de findAll().stream().filter()
        return rotinaRepository.findByIdosoIdAndAtivoTrueOrderByHorarioAsc(idosoId)
                .stream()
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                .toList();
    }

    public List<RotinaDTO> findByTipo(String tipo) {
        log.debug("Buscando rotinas por tipo: {}", tipo);
        try {
            Rotina.TipoRotina tipoEnum = Rotina.TipoRotina.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Agora usando consulta específica do banco
            return rotinaRepository.findByTipoAndAtivoTrueOrderByHorarioAsc(tipoEnum)
                    .stream()
                    .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                    .toList();
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de rotina inválido: {}", tipo);
            return List.of();
        }
    }

    public List<RotinaDTO> findByIdosoAndTipo(Long idosoId, String tipo) {
        log.debug("Buscando rotinas do idoso ID: {} por tipo: {}", idosoId, tipo);
        try {
            Rotina.TipoRotina tipoEnum = Rotina.TipoRotina.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco
            return rotinaRepository.findByIdosoIdAndTipoAndAtivoTrue(idosoId, tipoEnum)
                    .stream()
                    .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                    .toList();
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de rotina inválido: {}", tipo);
            return List.of();
        }
    }

    public List<RotinaDTO> findRotinasPorIntervaloHorario(Long idosoId, LocalTime horarioInicio, LocalTime horarioFim) {
        log.debug("Buscando rotinas do idoso ID: {} entre {} e {}", idosoId, horarioInicio, horarioFim);
        // OTIMIZAÇÃO: Usando consulta específica do banco
        return rotinaRepository.findRotinasPorIntervaloHorario(idosoId, horarioInicio, horarioFim)
                .stream()
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                .toList();
    }

    public List<RotinaDTO> findRotinasPorDiaSemana(Long idosoId, String diaSemana) {
        log.debug("Buscando rotinas do idoso ID: {} para o dia: {}", idosoId, diaSemana);
        // OTIMIZAÇÃO: Usando consulta específica do banco
        return rotinaRepository.findRotinasPorDiaSemana(idosoId, diaSemana)
                .stream()
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                .toList();
    }

    @Transactional
    public RotinaDTO create(RotinaDTO rotinaDTO) {
        log.debug("Criando nova rotina para idoso ID: {}", rotinaDTO.getIdosoId());

        Rotina rotina = modelMapper.map(rotinaDTO, Rotina.class);
        rotina.setId(null); // Garante que é uma nova rotina
        rotina.setAtivo(true); // Define como ativo por padrão

        Rotina savedRotina = rotinaRepository.save(rotina);
        log.info("Rotina criada com ID: {} para idoso ID: {}", savedRotina.getId(), rotinaDTO.getIdosoId());

        return modelMapper.map(savedRotina, RotinaDTO.class);
    }

    @Transactional
    public RotinaDTO create(RotinaCreateDTO rotinaCreateDTO) {
        log.debug("Criando nova rotina para idoso ID: {}", rotinaCreateDTO.getIdosoId());

        Rotina rotina = modelMapper.map(rotinaCreateDTO, Rotina.class);
        rotina.setId(null);
        rotina.setAtivo(true);

        Rotina savedRotina = rotinaRepository.save(rotina);
        log.info("Rotina criada com ID: {} para idoso ID: {}", savedRotina.getId(), rotinaCreateDTO.getIdosoId());

        return modelMapper.map(savedRotina, RotinaDTO.class);
    }

    @Transactional
    public RotinaDTO update(Long id, RotinaDTO rotinaDTO) {
        log.debug("Atualizando rotina ID: {}", id);

        Rotina rotina = rotinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rotina não encontrada com ID: " + id));

        modelMapper.map(rotinaDTO, rotina);
        rotina.setId(id); // Mantém o ID original

        Rotina updatedRotina = rotinaRepository.save(rotina);
        log.info("Rotina ID: {} atualizada com sucesso", id);

        return modelMapper.map(updatedRotina, RotinaDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Removendo rotina ID: {}", id);

        if (!rotinaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rotina não encontrada com ID: " + id);
        }

        rotinaRepository.deleteById(id);
        log.info("Rotina ID: {} removida com sucesso", id);
    }

    @Transactional
    public void deactivate(Long id) {
        log.debug("Desativando rotina ID: {}", id);

        Rotina rotina = rotinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rotina não encontrada com ID: " + id));

        rotina.setAtivo(false);
        rotinaRepository.save(rotina);
        log.info("Rotina ID: {} desativada com sucesso", id);
    }

    public long countRotinasByIdoso(Long idosoId) {
        return rotinaRepository.countByIdosoIdAndAtivoTrue(idosoId);
    }

    public List<RotinaDTO> findRotinasNoHorario(LocalTime horario) {
        log.debug("Buscando rotinas no horário: {}", horario);
        return rotinaRepository.findRotinasNoHorario(horario)
                .stream()
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                .toList();
    }

    @Transactional
    public RotinaDTO marcarExecucao(Long id) {
        log.debug("Marcando execução da rotina ID: {}", id);

        Rotina rotina = rotinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rotina não encontrada com ID: " + id));

        // Aqui você poderia criar uma entidade ExecucaoRotina para registrar execuções
        // Por enquanto, vamos apenas logar a execução
        log.info("Rotina ID: {} marcada como executada", id);

        return modelMapper.map(rotina, RotinaDTO.class);
    }

    public List<RotinaDTO> findRotinasPendentes(String data) {
        log.debug("Buscando rotinas pendentes para a data: {}", data);

        // Implementação simplificada - retorna todas as rotinas ativas
        // Em uma implementação completa, seria necessário verificar as execuções
        return rotinaRepository.findByAtivoTrue(null)
                .stream()
                .map(rotina -> modelMapper.map(rotina, RotinaDTO.class))
                .toList();
    }
}
