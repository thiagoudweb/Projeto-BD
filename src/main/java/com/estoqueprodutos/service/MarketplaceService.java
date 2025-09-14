package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.model.*;
import com.estoqueprodutos.model.dto.PedidoCompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MarketplaceService {

    private final IProdutoDAO produtoDAO;
    private final IPedidoDAO pedidoDAO;
    private final IClienteDAO clienteDAO;
    private final IPedidosProdutoDAO pedidosProdutoDAO;

    @Autowired
    public MarketplaceService(IProdutoDAO produtoDAO, IPedidoDAO pedidoDAO,
                            IClienteDAO clienteDAO, IPedidosProdutoDAO pedidosProdutoDAO) {
        this.produtoDAO = produtoDAO;
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
        this.pedidosProdutoDAO = pedidosProdutoDAO;
    }

    /**
     * Lista todos os produtos disponíveis para compra
     */
    public List<Produto> listarProdutosDisponiveis() {
        return produtoDAO.findAll();
    }

    /**
     * Processa um pedido de compra do marketplace
     */
    @Transactional
    public Pedido processarCompra(PedidoCompraDTO pedidoCompraDTO) {
        // Validar se o cliente existe
        Optional<Cliente> clienteOpt = clienteDAO.findById(pedidoCompraDTO.getIdCliente());
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + pedidoCompraDTO.getIdCliente() + " não encontrado.");
        }
        Cliente cliente = clienteOpt.get();

        // Criar o pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setPrazoEntrega(LocalDate.now().plusDays(15)); // Prazo de 15 dias
        pedido.setStatus("PENDENTE");
        pedido.setModoEncomenda(pedidoCompraDTO.getModoEncomenda() != null ?
                              pedidoCompraDTO.getModoEncomenda() : "ONLINE");

        // Calcular preço total e validar produtos
        BigDecimal precoTotal = BigDecimal.ZERO;

        for (PedidoCompraDTO.ItemCompraDTO item : pedidoCompraDTO.getItens()) {
            Optional<Produto> produtoOpt = produtoDAO.findById(item.getIdProduto());
            if (produtoOpt.isEmpty()) {
                throw new IllegalArgumentException("Produto com ID " + item.getIdProduto() + " não encontrado.");
            }

            Produto produto = produtoOpt.get();

            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero para o produto " + item.getIdProduto());
            }

            // Calcular preço do item (quantidade * preço unitário)
            BigDecimal precoItem = produto.getPrecoProduto().multiply(BigDecimal.valueOf(item.getQuantidade()));
            precoTotal = precoTotal.add(precoItem);
        }

        pedido.setPrecoFinal(precoTotal);

        // Salvar o pedido
        Pedido pedidoSalvo = pedidoDAO.save(pedido);

        // Criar os itens do pedido (PedidosProduto)
        for (PedidoCompraDTO.ItemCompraDTO item : pedidoCompraDTO.getItens()) {
            Produto produto = produtoDAO.findById(item.getIdProduto()).get(); // Já validamos acima

            PedidosProduto pedidosProduto = new PedidosProduto();

            // Criar ID composto
            PedidosProdutoId id = new PedidosProdutoId();
            id.setIdPedido(pedidoSalvo.getIdPedido());
            id.setIdProduto(item.getIdProduto());
            pedidosProduto.setId(id);

            pedidosProduto.setIdPedido(pedidoSalvo);
            pedidosProduto.setIdProduto(produto);
            pedidosProduto.setQuantidade(item.getQuantidade());

            // Calcular preço total dos produtos deste item
            BigDecimal precoTotalItem = produto.getPrecoProduto().multiply(BigDecimal.valueOf(item.getQuantidade()));
            pedidosProduto.setPrecoTotalProdutos(precoTotalItem);

            // Salvar o item do pedido
            pedidosProdutoDAO.save(pedidosProduto);
        }

        return pedidoSalvo;
    }

    /**
     * Busca um produto específico por ID
     */
    public Optional<Produto> buscarProduto(Integer id) {
        return produtoDAO.findById(id);
    }
}
