package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.ProdutosFornecedore;
import com.estoqueprodutos.model.ProdutosFornecedoreId;
import java.util.List;
import java.util.Optional;

public interface IProdutosFornecedoreDAO {
    ProdutosFornecedore save(ProdutosFornecedore produtosFornecedore);
    ProdutosFornecedore update(ProdutosFornecedore produtosFornecedore);
    void deleteById(ProdutosFornecedoreId id);
    Optional<ProdutosFornecedore> findById(ProdutosFornecedoreId id);
    List<ProdutosFornecedore> findAll();
}
