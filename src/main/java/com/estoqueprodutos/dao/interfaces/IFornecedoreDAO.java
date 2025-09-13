package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Fornecedore;
import java.util.List;
import java.util.Optional;

public interface IFornecedoreDAO {
    Fornecedore save(Fornecedore fornecedore);
    Fornecedore update(Fornecedore fornecedore);
    void deleteById(Integer id);
    Optional<Fornecedore> findById(Integer id);
    List<Fornecedore> findAll();
}
