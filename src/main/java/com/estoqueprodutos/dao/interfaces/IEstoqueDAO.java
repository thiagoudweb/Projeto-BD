package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Estoque;
import java.util.List;
import java.util.Optional;

public interface IEstoqueDAO {
    Estoque save(Estoque estoque);
    Estoque update(Estoque estoque);
    void deleteById(Integer id);
    Optional<Estoque> findById(Integer id);
    List<Estoque> findAll();
}
