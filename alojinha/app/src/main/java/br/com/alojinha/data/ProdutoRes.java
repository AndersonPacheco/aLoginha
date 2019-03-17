package br.com.alojinha.data;

import java.util.List;

import br.com.alojinha.model.Produto;

public class ProdutoRes extends AbstractRes {

    private List<Produto> produtoList;

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }
}
