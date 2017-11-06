package com.tmall.pokemon.bulbasaur.task.exception;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 下午2:25
 */


public class TaskException extends RuntimeException {
    private static final long serialVersionUID = 961555366397825465L;

    public TaskException(String s) {
        super(s);
    }

    public TaskException(Exception exception) {
        super(exception);
    }
}
