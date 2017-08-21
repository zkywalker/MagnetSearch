package org.zky.tool.magnetsearch.network;

/**
 * Created by zhangkun on 2017/3/16.
 */

public class ResponseState<T> {

    /**
     * result : {"statusCode":2,"message":"发送成功"}
     * status : 2
     * mes :
     */
    private T result;

    private int status;
    private String mes;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseState{" +
                "result=" + result +
                ", status=" + status +
                ", mes='" + mes + '\'' +
                '}';
    }
}
