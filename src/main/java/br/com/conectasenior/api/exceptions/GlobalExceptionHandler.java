package br.com.conectasenior.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Tratador global de exceções da API
 *
 * Centraliza o tratamento de erros garantindo respostas padronizadas
 * e logs adequados para monitoramento e debugging.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de campos (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Erro de validação: {}", ex.getMessage());

        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            "Dados inválidos fornecidos",
            request.getRequestURI(),
            details
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata violações de constraints do banco de dados
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("Violação de constraint: {}", ex.getMessage());

        List<String> details = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation ->
            details.add(violation.getPropertyPath() + ": " + violation.getMessage())
        );

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Violação de Constraint",
            "Dados violam regras de negócio",
            request.getRequestURI(),
            details
        );

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata recursos não encontrados
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());

        ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Recurso Não Encontrado",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.notFound().build();
    }

    /**
     * Trata violações de integridade de dados
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Violação de integridade de dados: {}", ex.getMessage());

        String message = "Operação viola regras de integridade dos dados";
        if (ex.getMessage().contains("unique")) {
            message = "Dados já existem no sistema";
        } else if (ex.getMessage().contains("foreign key")) {
            message = "Operação viola relacionamentos existentes";
        }

        ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            "Conflito de Dados",
            message,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Trata erros de autenticação
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Tentativa de autenticação inválida: {}", ex.getMessage());

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciais Inválidas",
            "Email ou senha incorretos",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Trata erros de autorização
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Acesso negado: {}", ex.getMessage());

        ApiError error = new ApiError(
            HttpStatus.FORBIDDEN.value(),
            "Acesso Negado",
            "Você não tem permissão para acessar este recurso",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Trata exceções gerais não mapeadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Erro interno não tratado: ", ex);

        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno",
            "Ocorreu um erro interno no servidor",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
