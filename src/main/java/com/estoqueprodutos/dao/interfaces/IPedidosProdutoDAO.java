package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.PedidosProduto;
import com.estoqueprodutos.model.PedidosProdutoId;

import java.util.List;
import java.util.Optional;

public interface IPedidosProdutoDAO {
    PedidosProduto save(PedidosProduto pedidosProduto);
    Optional<PedidosProduto> findById(PedidosProdutoId id);
    List<PedidosProduto> findAll();
    List<PedidosProduto> findByPedidoId(Integer pedidoId);
    void deleteById(PedidosProdutoId id);
}
