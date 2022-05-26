package com.alibaba.pokemon.bulbasaur.persist.domain;

import java.io.Serializable;

public class InvokeResult<T> implements Serializable {
    private static final long serialVersionUID = -3042551362183044652L;
    private String info;
    private boolean success = true;
    private Exception exception;
    private T resultData;

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
