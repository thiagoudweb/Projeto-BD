package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IEstoqueDAO;
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
    private final IEstoqueDAO estoqueDAO;

    @Autowired
    public MarketplaceService(IProdutoDAO produtoDAO, IPedidoDAO pedidoDAO,
                            IClienteDAO clienteDAO, IPedidosProdutoDAO pedidosProdutoDAO,
                            IEstoqueDAO estoqueDAO) {
        this.produtoDAO = produtoDAO;
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
        this.pedidosProdutoDAO = pedidosProdutoDAO;
        this.estoqueDAO = estoqueDAO;
    }

    /**
     * Lista todos os produtos disponíveis para compra
     */
    public List<Produto> listarProdutosDisponiveis() {
        List<Produto> produtos = produtoDAO.findAll();

        // Para cada produto, adicionar informação de estoque
        for (Produto produto : produtos) {
            int quantidadeDisponivel = verificarEstoqueDisponivel(produto.getIdProduto());

            // Se não tem estoque, marcar como indisponível
            if (quantidadeDisponivel <= 0) {
                produto.setStatus("INDISPONIVEL");
            }
        }

        return produtos;
    }

    /**
     * Processa um pedido de compra do marketplace com verificação de estoque
     */
    @Transactional
    public Pedido processarCompra(PedidoCompraDTO pedidoCompraDTO) {
        // Validar se o cliente existe
        Optional<Cliente> clienteOpt = clienteDAO.findById(pedidoCompraDTO.getIdCliente());
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + pedidoCompraDTO.getIdCliente() + " não encontrado.");
        }
        Cliente cliente = clienteOpt.get();

        // PRIMEIRA FASE: Validar disponibilidade de estoque para todos os produtos
        for (PedidoCompraDTO.ItemCompraDTO item : pedidoCompraDTO.getItens()) {
            Optional<Produto> produtoOpt = produtoDAO.findById(item.getIdProduto());
            if (produtoOpt.isEmpty()) {
                throw new IllegalArgumentException("Produto com ID " + item.getIdProduto() + " não encontrado.");
            }

            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero para o produto " + item.getIdProduto());
            }

            // Verificar se há estoque suficiente
            int quantidadeDisponivel = verificarEstoqueDisponivel(item.getIdProduto());
            if (quantidadeDisponivel < item.getQuantidade()) {
                throw new IllegalArgumentException(
                    String.format("Estoque insuficiente para o produto %d. Disponível: %d, Solicitado: %d",
                                item.getIdProduto(), quantidadeDisponivel, item.getQuantidade())
                );
            }
        }

        // Criar o pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setPrazoEntrega(LocalDate.now().plusDays(15)); // Prazo de 15 dias
        pedido.setStatus("pENDENTE");
        pedido.setModoEncomenda(pedidoCompraDTO.getModoEncomenda() != null ?
                              pedidoCompraDTO.getModoEncomenda() : "ONLINE");

        // Calcular preço total
        BigDecimal precoTotal = BigDecimal.ZERO;

        for (PedidoCompraDTO.ItemCompraDTO item : pedidoCompraDTO.getItens()) {
            Produto produto = produtoDAO.findById(item.getIdProduto()).get(); // Já validamos acima

            // Calcular preço do item (quantidade * preço unitário)
            BigDecimal precoItem = produto.getPrecoProduto().multiply(BigDecimal.valueOf(item.getQuantidade()));
            precoTotal = precoTotal.add(precoItem);
        }

        pedido.setPrecoFinal(precoTotal);

        // Salvar o pedido
        Pedido pedidoSalvo = pedidoDAO.save(pedido);

        // SEGUNDA FASE: Decrementar estoque e criar itens do pedido
        for (PedidoCompraDTO.ItemCompraDTO item : pedidoCompraDTO.getItens()) {
            Produto produto = produtoDAO.findById(item.getIdProduto()).get(); // Já validamos acima

            // Decrementar estoque
            decrementarEstoque(item.getIdProduto(), item.getQuantidade());

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
     * Verifica a quantidade total disponível em estoque para um produto
     */
    private int verificarEstoqueDisponivel(Integer idProduto) {
        List<Estoque> estoques = estoqueDAO.findByProdutoId(idProduto);
        return estoques.stream()
                      .mapToInt(Estoque::getQuantidadeExistente)
                      .sum();
    }

    /**
     * Decrementa a quantidade em estoque para um produto
     */
    private void decrementarEstoque(Integer idProduto, Integer quantidadeComprada) {
        List<Estoque> estoques = estoqueDAO.findByProdutoId(idProduto);

        int quantidadeRestante = quantidadeComprada;

        for (Estoque estoque : estoques) {
            if (quantidadeRestante <= 0) {
                break;
            }

            int quantidadeDisponivel = estoque.getQuantidadeExistente();

            if (quantidadeDisponivel >= quantidadeRestante) {
                // Este estoque tem suficiente para cobrir o restante
                estoque.setQuantidadeExistente(quantidadeDisponivel - quantidadeRestante);
                estoqueDAO.update(estoque);
                quantidadeRestante = 0;
            } else {
                // Este estoque será totalmente consumido
                estoque.setQuantidadeExistente(0);
                estoqueDAO.update(estoque);
                quantidadeRestante -= quantidadeDisponivel;
            }
        }

        if (quantidadeRestante > 0) {
            throw new IllegalStateException("Erro interno: não foi possível decrementar estoque suficiente");
        }
    }

    /**
     * Busca um produto específico por ID
     */
    public Optional<Produto> buscarProduto(Integer id) {
        return produtoDAO.findById(id);
    }
}
