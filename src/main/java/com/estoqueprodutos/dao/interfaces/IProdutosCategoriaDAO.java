package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.ProdutosCategoria;
import com.estoqueprodutos.model.ProdutosCategoriaId;
import java.util.List;
import java.util.Optional;

public interface IProdutosCategoriaDAO {
    ProdutosCategoria save(ProdutosCategoria produtosCategoria);
    ProdutosCategoria update(ProdutosCategoria produtosCategoria);
    void deleteById(ProdutosCategoriaId id);
    Optional<ProdutosCategoria> findById(ProdutosCategoriaId id);
    List<ProdutosCategoria> findAll();
}
