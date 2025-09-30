package br.com.conectasenior.api.controllers;

import br.com.conectasenior.api.dtos.RegistroSaudeDTO;
import br.com.conectasenior.api.services.SaudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações com Registros de Saúde
 */
@RestController
@RequestMapping("/api/saude")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Saúde", description = "API para gerenciamento de registros de saúde dos idosos")
public class SaudeController {

    private final SaudeService saudeService;

    @Operation(summary = "Lista todos os registros de saúde")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public Page<RegistroSaudeDTO> findAll(@PageableDefault(size = 20, sort = "dataRegistro") Pageable pageable) {
        log.info("GET /api/saude - Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return saudeService.findAll(pageable);
    }

    @Operation(summary = "Busca registro de saúde por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO', 'FAMILIAR')")
    public RegistroSaudeDTO findById(@Parameter(description = "ID do registro") @PathVariable Long id) {
        log.info("GET /api/saude/{}", id);
        return saudeService.findById(id);
    }

    @Operation(summary = "Busca registros de saúde por idoso")
    @GetMapping("/idoso/{idosoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO', 'FAMILIAR')")
    public Page<RegistroSaudeDTO> findByIdoso(
            @Parameter(description = "ID do idoso") @PathVariable Long idosoId,
            @PageableDefault(size = 50, sort = "dataRegistro") Pageable pageable) {
        log.info("GET /api/saude/idoso/{}", idosoId);
        return saudeService.findByIdoso(idosoId, pageable);
    }

    @Operation(summary = "Busca registros por tipo")
    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<RegistroSaudeDTO> findByTipo(@Parameter(description = "Tipo do registro") @PathVariable String tipo) {
        log.info("GET /api/saude/tipo/{}", tipo);
        return saudeService.findByTipo(tipo);
    }

    @Operation(summary = "Cria novo registro de saúde")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public RegistroSaudeDTO create(@Valid @RequestBody RegistroSaudeDTO registroSaudeDTO) {
        log.info("POST /api/saude - Criando registro para idoso: {}", registroSaudeDTO.getIdosoId());
        return saudeService.create(registroSaudeDTO);
    }

    @Operation(summary = "Atualiza registro de saúde")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public RegistroSaudeDTO update(@Parameter(description = "ID do registro") @PathVariable Long id,
                                  @Valid @RequestBody RegistroSaudeDTO registroSaudeDTO) {
        log.info("PUT /api/saude/{} - Atualizando registro", id);
        return saudeService.update(id, registroSaudeDTO);
    }

    @Operation(summary = "Remove registro de saúde")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MEDICO')")
    public void delete(@Parameter(description = "ID do registro") @PathVariable Long id) {
        log.info("DELETE /api/saude/{}", id);
        saudeService.delete(id);
    }

    @Operation(summary = "Busca registros por período")
    @GetMapping("/periodo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<RegistroSaudeDTO> findByPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam(required = false) Long idosoId) {
        log.info("GET /api/saude/periodo?dataInicio={}&dataFim={}&idosoId={}", dataInicio, dataFim, idosoId);
        return saudeService.findByPeriodo(dataInicio, dataFim, idosoId);
    }

    @Operation(summary = "Busca registros críticos")
    @GetMapping("/criticos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<RegistroSaudeDTO> findRegistrosCriticos() {
        log.info("GET /api/saude/criticos");
        return saudeService.findRegistrosCriticos();
    }

    @Operation(summary = "Gera relatório de saúde")
    @GetMapping("/relatorio/{idosoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public Object gerarRelatorio(@Parameter(description = "ID do idoso") @PathVariable Long idosoId,
                                @RequestParam(defaultValue = "30") int dias) {
        log.info("GET /api/saude/relatorio/{}?dias={}", idosoId, dias);
        return saudeService.gerarRelatorio(idosoId, dias);
    }
}
