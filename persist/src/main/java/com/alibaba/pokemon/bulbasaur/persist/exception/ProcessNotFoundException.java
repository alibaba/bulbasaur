package com.alibaba.pokemon.bulbasaur.persist.exception;

/**
 * @author yunche.ch@taobao.com
 * @date 2012-12-19 上午11:34:01
 */
public class ProcessNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public ProcessNotFoundException() {
    }

    public ProcessNotFoundException(String message) { // 用来创建指定参数对象
        super(message);
    }

    public ProcessNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ProcessNotFoundException(Throwable cause) {
        super(cause);
    }
}
