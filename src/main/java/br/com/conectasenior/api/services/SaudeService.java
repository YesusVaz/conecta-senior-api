package br.com.conectasenior.api.services;

import br.com.conectasenior.api.dtos.RegistroSaudeCreateDTO;
import br.com.conectasenior.api.dtos.RegistroSaudeDTO;
import br.com.conectasenior.api.entities.RegistroSaude;
import br.com.conectasenior.api.exceptions.ResourceNotFoundException;
import br.com.conectasenior.api.repositories.RegistroSaudeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service para operações com Registros de Saúde
 *
 * Gerencia CRUD e operações específicas do domínio de registros de saúde,
 * seguindo as melhores práticas do Spring Boot.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SaudeService {

    private final RegistroSaudeRepository registroSaudeRepository;
    private final ModelMapper modelMapper;

    public Page<RegistroSaudeDTO> findAll(Pageable pageable) {
        log.debug("Buscando todos os registros de saúde - Página: {}", pageable.getPageNumber());
        return registroSaudeRepository.findAll(pageable)
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class));
    }

    public RegistroSaudeDTO findById(Long id) {
        log.debug("Buscando registro de saúde por ID: {}", id);
        RegistroSaude registro = registroSaudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de saúde não encontrado com ID: " + id));
        return modelMapper.map(registro, RegistroSaudeDTO.class);
    }

    public Page<RegistroSaudeDTO> findByIdoso(Long idosoId, Pageable pageable) {
        log.debug("Buscando registros de saúde do idoso ID: {}", idosoId);
        // OTIMIZAÇÃO: Usando consulta específica do banco ao invés de findAll().stream().filter()
        return registroSaudeRepository.findByIdosoIdOrderByRegistradoEmDesc(idosoId, pageable)
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class));
    }

    public List<RegistroSaudeDTO> findByTipo(String tipo) {
        log.debug("Buscando registros de saúde por tipo: {}", tipo);
        try {
            RegistroSaude.TipoRegistro tipoEnum = RegistroSaude.TipoRegistro.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco
            return registroSaudeRepository.findByTipoOrderByRegistradoEmDesc(tipoEnum, null)
                    .stream()
                    .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                    .toList();
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de registro inválido: {}", tipo);
            return List.of();
        }
    }

    public List<RegistroSaudeDTO> findByIdosoAndTipo(Long idosoId, String tipo) {
        log.debug("Buscando registros de saúde do idoso ID: {} por tipo: {}", idosoId, tipo);
        try {
            RegistroSaude.TipoRegistro tipoEnum = RegistroSaude.TipoRegistro.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco
            return registroSaudeRepository.findByIdosoIdAndTipoOrderByRegistradoEmDesc(idosoId, tipoEnum)
                    .stream()
                    .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                    .toList();
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de registro inválido: {}", tipo);
            return List.of();
        }
    }

    public List<RegistroSaudeDTO> findRegistrosPorPeriodo(Long idosoId, LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Buscando registros de saúde do idoso ID: {} entre {} e {}", idosoId, inicio, fim);
        // OTIMIZAÇÃO: Usando consulta específica do banco
        return registroSaudeRepository.findRegistrosPorPeriodo(idosoId, inicio, fim)
                .stream()
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                .toList();
    }

    public List<RegistroSaudeDTO> findRegistrosRecentes(Long idosoId, int horas) {
        log.debug("Buscando registros recentes do idoso ID: {} das últimas {} horas", idosoId, horas);
        LocalDateTime dataLimite = LocalDateTime.now().minusHours(horas);
        // OTIMIZAÇÃO: Usando consulta específica do banco
        return registroSaudeRepository.findRegistrosRecentes(idosoId, dataLimite)
                .stream()
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                .toList();
    }

    public List<RegistroSaudeDTO> findRegistrosCriticos(Long idosoId, RegistroSaude.TipoRegistro tipo, Double minimo, Double maximo) {
        log.debug("Buscando registros críticos do idoso ID: {} para tipo: {}", idosoId, tipo);
        // OTIMIZAÇÃO: Usando consulta específica do banco
        return registroSaudeRepository.findRegistrosForaDaFaixa(idosoId, tipo, minimo, maximo)
                .stream()
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                .toList();
    }

    public Optional<RegistroSaudeDTO> findUltimoRegistroPorTipo(Long idosoId, String tipo) {
        log.debug("Buscando último registro do tipo: {} para idoso ID: {}", tipo, idosoId);
        try {
            RegistroSaude.TipoRegistro tipoEnum = RegistroSaude.TipoRegistro.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco
            return registroSaudeRepository.findUltimoRegistroPorTipo(idosoId, tipoEnum)
                    .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class));
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de registro inválido: {}", tipo);
            return Optional.empty();
        }
    }

    public Double calcularMediaPorTipoEPeriodo(Long idosoId, String tipo, LocalDateTime inicio, LocalDateTime fim) {
        log.debug("Calculando média do tipo: {} para idoso ID: {} entre {} e {}", tipo, idosoId, inicio, fim);
        try {
            RegistroSaude.TipoRegistro tipoEnum = RegistroSaude.TipoRegistro.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco para calcular média
            return registroSaudeRepository.findMediaValoresPorTipoEPeriodo(idosoId, tipoEnum, inicio, fim);
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de registro inválido: {}", tipo);
            return null;
        }
    }

    public Long countRegistrosPorTipo(Long idosoId, String tipo) {
        log.debug("Contando registros do tipo: {} para idoso ID: {}", tipo, idosoId);
        try {
            RegistroSaude.TipoRegistro tipoEnum = RegistroSaude.TipoRegistro.valueOf(tipo.toUpperCase());
            // OTIMIZAÇÃO: Usando consulta específica do banco para contar
            return registroSaudeRepository.countByIdosoIdAndTipo(idosoId, tipoEnum);
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de registro inválido: {}", tipo);
            return 0L;
        }
    }

    @Transactional
    public RegistroSaudeDTO create(RegistroSaudeDTO registroDTO) {
        log.debug("Criando novo registro de saúde para idoso ID: {}", registroDTO.getIdosoId());

        RegistroSaude registro = modelMapper.map(registroDTO, RegistroSaude.class);
        registro.setId(null); // Garante que é um novo registro
        if (registro.getRegistradoEm() == null) {
            registro.setRegistradoEm(LocalDateTime.now());
        }

        RegistroSaude savedRegistro = registroSaudeRepository.save(registro);
        log.info("Registro de saúde criado com ID: {} para idoso ID: {}",
                savedRegistro.getId(), registroDTO.getIdosoId());

        return modelMapper.map(savedRegistro, RegistroSaudeDTO.class);
    }

    @Transactional
    public RegistroSaudeDTO create(RegistroSaudeCreateDTO registroCreateDTO) {
        log.debug("Criando novo registro de saúde para idoso ID: {}", registroCreateDTO.getIdosoId());

        RegistroSaude registro = modelMapper.map(registroCreateDTO, RegistroSaude.class);
        registro.setId(null);
        if (registro.getRegistradoEm() == null) {
            registro.setRegistradoEm(LocalDateTime.now());
        }

        RegistroSaude savedRegistro = registroSaudeRepository.save(registro);
        log.info("Registro de saúde criado com ID: {} para idoso ID: {}",
                savedRegistro.getId(), registroCreateDTO.getIdosoId());

        return modelMapper.map(savedRegistro, RegistroSaudeDTO.class);
    }

    @Transactional
    public RegistroSaudeDTO update(Long id, RegistroSaudeDTO registroDTO) {
        log.debug("Atualizando registro de saúde ID: {}", id);

        RegistroSaude registro = registroSaudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de saúde não encontrado com ID: " + id));

        modelMapper.map(registroDTO, registro);
        registro.setId(id); // Mantém o ID original

        RegistroSaude updatedRegistro = registroSaudeRepository.save(registro);
        log.info("Registro de saúde ID: {} atualizado com sucesso", id);

        return modelMapper.map(updatedRegistro, RegistroSaudeDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Removendo registro de saúde ID: {}", id);

        if (!registroSaudeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro de saúde não encontrado com ID: " + id);
        }

        registroSaudeRepository.deleteById(id);
        log.info("Registro de saúde ID: {} removido com sucesso", id);
    }

    // Método adicional para compatibilidade com controllers existentes
    public List<RegistroSaudeDTO> findByPeriodo(String dataInicio, String dataFim, Long idosoId) {
        log.debug("Buscando registros por período: {} - {} para idoso ID: {}", dataInicio, dataFim, idosoId);

        // Implementação simplificada - em produção deveria converter as strings para LocalDateTime
        if (idosoId != null) {
            return findByIdoso(idosoId, null).getContent();
        }

        return findAll(null).getContent();
    }

    // Sobrecarga para compatibilidade - versão simplificada para alertas
    public List<RegistroSaudeDTO> findRegistrosCriticos() {
        log.debug("Buscando registros críticos (versão simplificada)");

        // Implementação básica - busca registros de pressão arterial com valores críticos
        return registroSaudeRepository.findAll()
                .stream()
                .filter(registro -> registro.getTipo() == RegistroSaude.TipoRegistro.PRESSAO_ARTERIAL)
                .filter(registro -> registro.getValor() != null &&
                        (registro.getValor().compareTo(new BigDecimal("140")) > 0 || registro.getValor().compareTo(new BigDecimal("90")) < 0))
                .map(registro -> modelMapper.map(registro, RegistroSaudeDTO.class))
                .toList();
    }

    public List<RegistroSaudeDTO> gerarRelatorio(Long idosoId, int dias) {
        log.debug("Gerando relatório para idoso ID: {} dos últimos {} dias", idosoId, dias);

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(dias);
        LocalDateTime agora = LocalDateTime.now();

        return findRegistrosPorPeriodo(idosoId, dataLimite, agora);
    }
}
