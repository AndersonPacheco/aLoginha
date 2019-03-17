package br.com.alojinha.data;

import java.util.List;

import br.com.alojinha.model.Categoria;

public class CategoriaRes extends AbstractRes{

    private List<Categoria> categoriaList;

    public List<Categoria> getCategoriaList() {
        return categoriaList;
    }

    public void setCategoriaList(List<Categoria> categoriaList) {
        this.categoriaList = categoriaList;
    }
}
