package br.com.conectasenior.api.controllers;

import br.com.conectasenior.api.dtos.RotinaDTO;
import br.com.conectasenior.api.services.RotinaService;
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
 * Controller REST para operações com Rotinas
 */
@RestController
@RequestMapping("/api/rotinas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rotinas", description = "API para gerenciamento de rotinas dos idosos")
public class RotinaController {

    private final RotinaService rotinaService;

    @Operation(summary = "Lista todas as rotinas")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public Page<RotinaDTO> findAll(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        log.info("GET /api/rotinas - Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return rotinaService.findAll(pageable);
    }

    @Operation(summary = "Busca rotina por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO', 'FAMILIAR')")
    public RotinaDTO findById(@Parameter(description = "ID da rotina") @PathVariable Long id) {
        log.info("GET /api/rotinas/{}", id);
        return rotinaService.findById(id);
    }

    @Operation(summary = "Busca rotinas por idoso")
    @GetMapping("/idoso/{idosoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO', 'FAMILIAR')")
    public List<RotinaDTO> findByIdoso(@Parameter(description = "ID do idoso") @PathVariable Long idosoId) {
        log.info("GET /api/rotinas/idoso/{}", idosoId);
        return rotinaService.findByIdoso(idosoId);
    }

    @Operation(summary = "Busca rotinas por tipo")
    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<RotinaDTO> findByTipo(@Parameter(description = "Tipo da rotina") @PathVariable String tipo) {
        log.info("GET /api/rotinas/tipo/{}", tipo);
        return rotinaService.findByTipo(tipo);
    }

    @Operation(summary = "Cria nova rotina")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public RotinaDTO create(@Valid @RequestBody RotinaDTO rotinaDTO) {
        log.info("POST /api/rotinas - Criando rotina: {}", rotinaDTO.getNome());
        return rotinaService.create(rotinaDTO);
    }

    @Operation(summary = "Atualiza rotina")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public RotinaDTO update(@Parameter(description = "ID da rotina") @PathVariable Long id,
                           @Valid @RequestBody RotinaDTO rotinaDTO) {
        log.info("PUT /api/rotinas/{} - Atualizando rotina", id);
        return rotinaService.update(id, rotinaDTO);
    }

    @Operation(summary = "Remove rotina")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR')")
    public void delete(@Parameter(description = "ID da rotina") @PathVariable Long id) {
        log.info("DELETE /api/rotinas/{}", id);
        rotinaService.delete(id);
    }

    @Operation(summary = "Marca execução de rotina")
    @PostMapping("/{id}/executar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'FAMILIAR')")
    public void marcarExecucao(@Parameter(description = "ID da rotina") @PathVariable Long id) {
        log.info("POST /api/rotinas/{}/executar", id);
        rotinaService.marcarExecucao(id);
    }

    @Operation(summary = "Busca rotinas pendentes por data")
    @GetMapping("/pendentes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<RotinaDTO> findRotinasPendentes(@RequestParam String data) {
        log.info("GET /api/rotinas/pendentes?data={}", data);
        return rotinaService.findRotinasPendentes(data);
    }
}
