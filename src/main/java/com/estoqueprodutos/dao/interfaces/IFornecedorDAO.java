package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Fornecedor;
import java.util.List;
import java.util.Optional;

public interface IFornecedorDAO {
    Fornecedor save(Fornecedor fornecedor);
    Fornecedor update(Fornecedor fornecedor);
    void deleteById(Integer id);
    Optional<Fornecedor> findById(Integer id);
    List<Fornecedor> findAll();
}