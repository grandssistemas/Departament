package br.com.codein.department.domain.model.exception;

/**
 * Created by gelatti on 21/02/17.
 */
public class ParamWrongException extends RuntimeException{

    /**
     * @param message mensagem que será emitada na exceção
     */
    public ParamWrongException(String message) {
        super(message);
    }
}
