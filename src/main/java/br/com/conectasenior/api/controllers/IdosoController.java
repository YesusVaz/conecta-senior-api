package br.com.conectasenior.api.controllers;

import br.com.conectasenior.api.dtos.IdosoDTO;
import br.com.conectasenior.api.services.IdosoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Controller REST para operações com Idosos
 *
 * Expõe endpoints para CRUD completo, busca paginada e operações
 * específicas do domínio. Serve como referência para outros controllers.
 */
@RestController
@RequestMapping("/api/idosos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Idosos", description = "API para gerenciamento de idosos")
public class IdosoController {

    private final IdosoService idosoService;

    @Operation(summary = "Lista todos os idosos", description = "Retorna lista paginada de todos os idosos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public Page<IdosoDTO> findAll(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        log.info("GET /api/idosos - Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return idosoService.findAll(pageable);
    }

    @Operation(summary = "Busca idoso por ID", description = "Retorna um idoso específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Idoso encontrado"),
        @ApiResponse(responseCode = "404", description = "Idoso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO', 'FAMILIAR')")
    public IdosoDTO findById(@Parameter(description = "ID do idoso") @PathVariable Long id) {
        log.info("GET /api/idosos/{}", id);
        return idosoService.findById(id);
    }

    @Operation(summary = "Busca idoso por CPF", description = "Retorna um idoso específico pelo seu CPF")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Idoso encontrado"),
        @ApiResponse(responseCode = "404", description = "Idoso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public IdosoDTO findByCpf(@Parameter(description = "CPF do idoso") @PathVariable String cpf) {
        log.info("GET /api/idosos/cpf/{}", cpf);
        return idosoService.findByCpf(cpf);
    }

    @Operation(summary = "Busca idosos por nome", description = "Retorna lista paginada de idosos que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public Page<IdosoDTO> findByNome(
            @Parameter(description = "Nome para busca") @RequestParam String nome,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        log.info("GET /api/idosos/buscar?nome={}", nome);
        return idosoService.findByNome(nome, pageable);
    }

    @Operation(summary = "Cria novo idoso", description = "Cadastra um novo idoso no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Idoso criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "CPF ou email já cadastrados"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR')")
    public IdosoDTO create(@Valid @RequestBody IdosoDTO idosoDTO) {
        log.info("POST /api/idosos - Criando idoso: {}", idosoDTO.getNome());
        return idosoService.create(idosoDTO);
    }

    @Operation(summary = "Atualiza idoso", description = "Atualiza dados de um idoso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Idoso atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Idoso não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR')")
    public IdosoDTO update(@Parameter(description = "ID do idoso") @PathVariable Long id,
                          @Valid @RequestBody IdosoDTO idosoDTO) {
        log.info("PUT /api/idosos/{} - Atualizando idoso", id);
        return idosoService.update(id, idosoDTO);
    }

    @Operation(summary = "Remove idoso", description = "Remove um idoso do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Idoso removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Idoso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void delete(@Parameter(description = "ID do idoso") @PathVariable Long id) {
        log.info("DELETE /api/idosos/{}", id);
        idosoService.delete(id);
    }

    @Operation(summary = "Busca idosos por faixa etária", description = "Retorna idosos dentro de uma faixa etária específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/idade")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<IdosoDTO> findByIdadeEntre(
            @Parameter(description = "Idade mínima") @RequestParam int idadeMinima,
            @Parameter(description = "Idade máxima") @RequestParam int idadeMaxima) {
        log.info("GET /api/idosos/idade?idadeMinima={}&idadeMaxima={}", idadeMinima, idadeMaxima);
        return idosoService.findByIdadeEntre(idadeMinima, idadeMaxima);
    }

    @Operation(summary = "Busca idosos com emergências ativas", description = "Retorna idosos que possuem emergências em andamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/emergencias-ativas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CUIDADOR', 'MEDICO')")
    public List<IdosoDTO> findIdososComEmergenciasAtivas() {
        log.info("GET /api/idosos/emergencias-ativas");
        return idosoService.findIdososComEmergenciasAtivas();
    }
}
