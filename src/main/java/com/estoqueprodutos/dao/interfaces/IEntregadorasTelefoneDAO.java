package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.EntregadorasTelefone;
import com.estoqueprodutos.model.EntregadorasTelefoneId;
import java.util.List;
import java.util.Optional;

public interface IEntregadorasTelefoneDAO {
    EntregadorasTelefone save(EntregadorasTelefone entregadorasTelefone);
    EntregadorasTelefone update(EntregadorasTelefone entregadorasTelefone);
    void deleteById(EntregadorasTelefoneId id);
    Optional<EntregadorasTelefone> findById(EntregadorasTelefoneId id);
    List<EntregadorasTelefone> findAll();
}
