package br.com.conectasenior.api.controllers;

import br.com.conectasenior.api.dtos.UsuarioDTO;
import br.com.conectasenior.api.dtos.requests.LoginRequest;
import br.com.conectasenior.api.dtos.requests.RegistroRequest;
import br.com.conectasenior.api.dtos.responses.LoginResponse;
import br.com.conectasenior.api.dtos.responses.TokenResponse;
import br.com.conectasenior.api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operações de autenticação
 *
 * Gerencia login, registro e operações relacionadas à autenticação JWT.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "API para autenticação e gerenciamento de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login do usuário", description = "Autentica usuário e retorna token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("POST /api/auth/login - Tentativa de login para: {}", loginRequest.getEmail());

        return authService.login(loginRequest);
    }

    @Operation(summary = "Registro de usuário", description = "Cadastra novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO registro(@Valid @RequestBody RegistroRequest registroRequest) {
        log.info("POST /api/auth/registro - Novo registro de usuário: {}", registroRequest.getEmail());

        return authService.registro(registroRequest);
    }

    @Operation(summary = "Refresh token", description = "Gera novo token a partir de um token válido")
    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestHeader("Authorization") String token) {
        log.info("POST /api/auth/refresh - Refresh token");

        return authService.refreshToken(token);
    }
}
