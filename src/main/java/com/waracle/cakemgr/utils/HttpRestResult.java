package com.waracle.cakemgr.utils;

public class HttpRestResult<Integer, R> {
    private final Integer responseCode;
    private final R response;

    public HttpRestResult(Integer responseCode, R response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public R getResponse() {
        return response;
    }
}
