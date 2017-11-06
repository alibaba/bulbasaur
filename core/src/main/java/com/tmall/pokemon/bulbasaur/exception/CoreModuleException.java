package com.tmall.pokemon.bulbasaur.exception;

/**
 * 专有异常
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-14 上午11:13:43
 */
public class CoreModuleException extends RuntimeException {
    private static final long serialVersionUID = -5604497198924793181L;

    public CoreModuleException() {
    }

    public CoreModuleException(String message) {
        super(message);
    }

    public CoreModuleException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CoreModuleException(Throwable cause) {
        super(cause);
    }

}
