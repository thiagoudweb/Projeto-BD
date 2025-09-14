package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.ProdutosFornecedores;
import com.estoqueprodutos.model.ProdutosFornecedoresId;
import java.util.List;
import java.util.Optional;

public interface IProdutosFornecedoresDAO {
    ProdutosFornecedores save(ProdutosFornecedores produtosFornecedores);
    ProdutosFornecedores update(ProdutosFornecedores produtosFornecedores);
    void deleteById(ProdutosFornecedoresId id);
    Optional<ProdutosFornecedores> findById(ProdutosFornecedoresId id);
    List<ProdutosFornecedores> findAll();

    // Métodos específicos para fornecedor
    List<ProdutosFornecedores> findByFornecedorId(Integer fornecedorId);
    void deleteByFornecedorIdAndProdutoId(Integer fornecedorId, Integer produtoId);
}