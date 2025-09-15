package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.model.PedidosProduto;
import com.estoqueprodutos.model.PedidosProdutoId;
import com.estoqueprodutos.model.Pedido;
import com.estoqueprodutos.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidosProdutoService {

    private final IPedidosProdutoDAO pedidosProdutoDAO;
    private final IPedidoDAO pedidoDAO;
    private final IProdutoDAO produtoDAO;

    @Autowired
    public PedidosProdutoService(IPedidosProdutoDAO pedidosProdutoDAO,
                                 IPedidoDAO pedidoDAO,
                                 IProdutoDAO produtoDAO) {
        this.pedidosProdutoDAO = pedidosProdutoDAO;
        this.pedidoDAO = pedidoDAO;
        this.produtoDAO = produtoDAO;
    }

    @Transactional
    public PedidosProduto salvar(PedidosProduto pedidosProduto) {
        // Validar se o pedido existe
        if (pedidosProduto.getId() != null && pedidosProduto.getId().getIdPedido() != null) {
            Optional<Pedido> pedido = pedidoDAO.findById(pedidosProduto.getId().getIdPedido());
            if (pedido.isEmpty()) {
                throw new IllegalArgumentException("Pedido com ID " + pedidosProduto.getId().getIdPedido() + " não encontrado.");
            }
            pedidosProduto.setIdPedido(pedido.get());
        }

        // Validar se o produto existe
        if (pedidosProduto.getId() != null && pedidosProduto.getId().getIdProduto() != null) {
            Optional<Produto> produto = produtoDAO.findById(pedidosProduto.getId().getIdProduto());
            if (produto.isEmpty()) {
                throw new IllegalArgumentException("Produto com ID " + pedidosProduto.getId().getIdProduto() + " não encontrado.");
            }
            pedidosProduto.setIdProduto(produto.get());
        }

        return pedidosProdutoDAO.save(pedidosProduto);
    }

    public List<PedidosProduto> listarTodos() {
        return pedidosProdutoDAO.findAll();
    }

    public Optional<PedidosProduto> buscarPorId(PedidosProdutoId id) {
        return pedidosProdutoDAO.findById(id);
    }

    public List<PedidosProduto> buscarPorPedidoId(Integer pedidoId) {
        return pedidosProdutoDAO.findByPedidoId(pedidoId);
    }

    @Transactional
    public void deletar(PedidosProdutoId id) {
        pedidosProdutoDAO.deleteById(id);
    }

    @Transactional
    public PedidosProduto adicionarProdutoAoPedido(Integer pedidoId, Integer produtoId, Integer quantidade) {
        // Criar novo PedidosProduto
        PedidosProduto pedidosProduto = new PedidosProduto();

        // Configurar ID composto
        PedidosProdutoId id = new PedidosProdutoId();
        id.setIdPedido(pedidoId);
        id.setIdProduto(produtoId);
        pedidosProduto.setId(id);

        // Configurar quantidade
        pedidosProduto.setQuantidade(quantidade);

        // Buscar e configurar entidades relacionadas
        Optional<Pedido> pedido = pedidoDAO.findById(pedidoId);
        Optional<Produto> produto = produtoDAO.findById(produtoId);

        if (pedido.isEmpty()) {
            throw new IllegalArgumentException("Pedido com ID " + pedidoId + " não encontrado.");
        }
        if (produto.isEmpty()) {
            throw new IllegalArgumentException("Produto com ID " + produtoId + " não encontrado.");
        }

        pedidosProduto.setIdPedido(pedido.get());
        pedidosProduto.setIdProduto(produto.get());

        // Calcular preço total (quantidade * preço unitário do produto)
        pedidosProduto.setPrecoTotalProdutos(
            produto.get().getPrecoProduto().multiply(java.math.BigDecimal.valueOf(quantidade))
        );

        return pedidosProdutoDAO.save(pedidosProduto);
    }
}
