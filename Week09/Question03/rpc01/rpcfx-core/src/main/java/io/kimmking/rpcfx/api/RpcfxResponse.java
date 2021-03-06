package io.kimmking.rpcfx.api;

import lombok.Data;

public class RpcfxResponse {
    private Object result;
    private boolean status;
    private Exception exception;

    public RpcfxResponse() {
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "RpcfxResponse{" +
                "result=" + result +
                ", status=" + status +
                ", exception=" + exception +
                '}';
    }
}
