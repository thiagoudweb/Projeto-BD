package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.ProdutoIdioma;
import java.util.List;
import java.util.Optional;

public interface IProdutoIdiomaDAO {
    ProdutoIdioma save(ProdutoIdioma produtoIdioma);
    ProdutoIdioma update(ProdutoIdioma produtoIdioma);
    Optional<ProdutoIdioma> findById(Integer id);
    List<ProdutoIdioma> findAll();
    List<ProdutoIdioma> findByProdutoId(Integer produtoId);
    void deleteById(Integer id);
    void deleteByProdutoId(Integer produtoId);
}
