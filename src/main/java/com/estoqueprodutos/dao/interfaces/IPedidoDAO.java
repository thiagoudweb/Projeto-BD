package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface IPedidoDAO {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(Integer id);

    List<Pedido> findAll();

    void deleteById(Integer id);

    List<Pedido> findByClienteId(Integer clienteId);
}
