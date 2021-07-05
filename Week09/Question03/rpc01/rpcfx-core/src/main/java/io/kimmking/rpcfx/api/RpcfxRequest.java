package io.kimmking.rpcfx.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.Arrays;

public class RpcfxRequest {
    private String serviceClass;
    private String method;
    private Object[] params;

    public RpcfxRequest() {
    }

    public RpcfxRequest(String content) {
        ObjectMapper omr = new ObjectMapper();
        try {
            RpcfxRequest rpcfxRequest = omr.readValue(content, RpcfxRequest.class);
            this.serviceClass = rpcfxRequest.getServiceClass();
            this.method = rpcfxRequest.getMethod();
            this.params = rpcfxRequest.getParams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RpcfxRequest{" +
                "serviceClass='" + serviceClass + '\'' +
                ", method='" + method + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
