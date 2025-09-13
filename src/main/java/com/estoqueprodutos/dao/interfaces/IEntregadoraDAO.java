package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Entregadora;
import java.util.List;
import java.util.Optional;

public interface IEntregadoraDAO {
    Entregadora save(Entregadora entregadora);
    Entregadora update(Entregadora entregadora);
    void deleteById(Integer id);
    Optional<Entregadora> findById(Integer id);
    List<Entregadora> findAll();
}
