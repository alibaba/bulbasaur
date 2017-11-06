package com.tmall.pokemon.bulbasaur.exception;

/**
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-7
 * Time: 下午3:39
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1265008583859018745L;

    public BizException() {
    }

    public BizException(String message) { // 用来创建指定参数对象
        super(message);
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
