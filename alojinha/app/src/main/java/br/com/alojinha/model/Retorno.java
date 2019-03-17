package br.com.alojinha.model;

import java.io.Serializable;

public class Retorno implements Serializable {

	private int statusCode;
	private String strRetorno;
    private String mensagem;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStrRetorno() {
        return strRetorno;
    }

    public void setStrRetorno(String strRetorno) {
        this.strRetorno = strRetorno;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
