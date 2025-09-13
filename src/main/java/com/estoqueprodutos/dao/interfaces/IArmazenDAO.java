package com.estoqueprodutos.dao.interfaces;

import java.util.List;
import java.util.Optional;

import com.estoqueprodutos.model.Armazen;

public interface IArmazenDAO {
    Armazen save(Armazen armazen);
    Armazen update(Armazen armazen);
    Optional<Armazen> findById(Integer id);
    List<Armazen> findAll();
    void deleteById(Integer id);
}
