package br.com.conectasenior.api.exceptions;

/**
 * Exceção para regras de negócio
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
