/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author AlexFess
 */
public class ClientRequestResult implements Serializable {

    private int error_code;
    private Object result;

    public ClientRequestResult() {
    }

    public ClientRequestResult(int error_code, Object result) {
        this.error_code = error_code;
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
