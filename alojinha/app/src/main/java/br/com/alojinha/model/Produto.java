package br.com.alojinha.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Produto implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("urlImagem")
    @Expose
    private String urlImagem;

    @SerializedName("descricao")
    @Expose
    private String descricao;

    @SerializedName("precoDe")
    @Expose
    private double precoDe;

    @SerializedName("precoPor")
    @Expose
    private double precoPor;

    @SerializedName("categoria")
    @Expose
    private Categoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoDe() {
        return precoDe;
    }

    public void setPrecoDe(double precoDe) {
        this.precoDe = precoDe;
    }

    public double getPrecoPor() {
        return precoPor;
    }

    public void setPrecoPor(double precoPor) {
        this.precoPor = precoPor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
