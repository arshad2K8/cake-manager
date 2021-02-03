package com.waracle.cakemgr.exceptions;

public class CakeManagerException extends Exception {
    private static final long serialVersionUID = 1L;

    private String message;

    public CakeManagerException(String message) {
        super();
        this.message = message;
    }

    public CakeManagerException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public CakeManagerException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
