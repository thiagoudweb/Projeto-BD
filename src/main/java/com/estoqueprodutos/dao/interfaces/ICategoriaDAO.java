package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaDAO {
    Categoria save(Categoria categoria);
    Categoria update(Categoria categoria);
    Optional<Categoria> findById(Integer id);
    List<Categoria> findAll();
    void deleteById(Integer id);
}
