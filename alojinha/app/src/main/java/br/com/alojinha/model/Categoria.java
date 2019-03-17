package br.com.alojinha.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Categoria implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("descricao")
    @Expose
    private String descricao;

    @SerializedName("urlImagem")
    @Expose
    private String urlImagem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
