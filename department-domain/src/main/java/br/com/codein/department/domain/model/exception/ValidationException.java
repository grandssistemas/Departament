package br.com.codein.department.domain.model.exception;

/**
 * Created by gelatti on 21/02/17.
 */
public class ValidationException extends RuntimeException{

    /**
     *
     * @param message mensagem que será emitida na exceção
     */
    public ValidationException(String message) {
        super(message);
    }
}
