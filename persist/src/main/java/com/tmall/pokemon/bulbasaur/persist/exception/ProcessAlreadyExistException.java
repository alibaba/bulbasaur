package com.tmall.pokemon.bulbasaur.persist.exception;

/**
 * @author yunche.ch@taobao.com
 * @date 2012-12-19 上午11:31:25
 */
public class ProcessAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public ProcessAlreadyExistException() {
    }

    public ProcessAlreadyExistException(String message) {
        super(message);

    }

    public ProcessAlreadyExistException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ProcessAlreadyExistException(Throwable cause) {
        super(cause);
    }
}