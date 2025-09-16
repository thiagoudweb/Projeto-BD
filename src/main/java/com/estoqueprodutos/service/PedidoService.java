package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.model.Pedido;
import com.estoqueprodutos.model.Cliente;
import com.estoqueprodutos.model.PedidosProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final IPedidoDAO pedidoDAO;
    private final IClienteDAO clienteDAO;
    private final IPedidosProdutoDAO pedidosProdutoDAO;

    @Autowired
    public PedidoService(IPedidoDAO pedidoDAO, IClienteDAO clienteDAO, IPedidosProdutoDAO pedidosProdutoDAO) {
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
        this.pedidosProdutoDAO = pedidosProdutoDAO;
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        // Se o pedido tem apenas o ID do cliente, buscar o cliente completo
        if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() != null) {
            Integer clienteId = pedido.getCliente().getIdCliente();
            Optional<Cliente> cliente = clienteDAO.findById(clienteId);

            if (cliente.isEmpty()) {
                throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
            }

            pedido.setCliente(cliente.get());
        } else {
            throw new IllegalArgumentException("Pedido deve estar associado a um cliente com ID válido.");
        }

        // Salvar o pedido
        Pedido pedidoSalvo = pedidoDAO.save(pedido);

        // Processar produtos do pedido se foram fornecidos
        if (pedido.getProdutos() != null && !pedido.getProdutos().isEmpty()) {
            for (PedidosProduto pedidoProduto : pedido.getProdutos()) {
                pedidoProduto.setIdPedido(pedidoSalvo);
                pedidosProdutoDAO.save(pedidoProduto);
            }
        }

        return pedidoSalvo;
    }

    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = pedidoDAO.findAll();
        // Popular IDs dos produtos para cada pedido
        for (Pedido pedido : pedidos) {
            popularIdsProdutos(pedido);
        }
        return pedidos;
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        Optional<Pedido> pedido = pedidoDAO.findById(id);
        if (pedido.isPresent()) {
            popularIdsProdutos(pedido.get());
        }
        return pedido;
    }

    public List<Pedido> buscarPorClienteId(Integer clienteId) {
        List<Pedido> pedidos = pedidoDAO.findByClienteId(clienteId);
        // Popular IDs dos produtos para cada pedido
        for (Pedido pedido : pedidos) {
            popularIdsProdutos(pedido);
        }
        return pedidos;
    }

    @Transactional
    public void deletar(Integer id) {
        // Primeiro, deletar todos os produtos relacionados ao pedido
        List<PedidosProduto> produtosRelacionados = pedidosProdutoDAO.findByPedidoId(id);
        for (PedidosProduto pedidoProduto : produtosRelacionados) {
            pedidosProdutoDAO.deleteById(pedidoProduto.getId());
        }

        // Agora é seguro deletar o pedido
        pedidoDAO.deleteById(id);
    }

    // Método privado para popular a lista de IDs dos produtos
    private void popularIdsProdutos(Pedido pedido) {
        List<PedidosProduto> pedidosProdutos = pedidosProdutoDAO.findByPedidoId(pedido.getIdPedido());
        List<Integer> idsProdutos = pedidosProdutos.stream()
                .map(pp -> pp.getIdProduto().getIdProduto())
                .collect(Collectors.toList());
        pedido.setIdsProdutos(idsProdutos);
    }

    // Método para buscar pedido com produtos (mantido para compatibilidade)
    public Optional<Pedido> buscarPedidoComProdutos(Integer id) {
        Optional<Pedido> pedido = pedidoDAO.findById(id);
        if (pedido.isPresent()) {
            List<PedidosProduto> produtos = pedidosProdutoDAO.findByPedidoId(id);
            pedido.get().setProdutos(produtos);
            popularIdsProdutos(pedido.get());
        }
        return pedido;
    }

    // Método para listar todos os pedidos com produtos (mantido para compatibilidade)
    public List<Pedido> listarTodosComProdutos() {
        List<Pedido> pedidos = pedidoDAO.findAll();
        for (Pedido pedido : pedidos) {
            List<PedidosProduto> produtos = pedidosProdutoDAO.findByPedidoId(pedido.getIdPedido());
            pedido.setProdutos(produtos);
            popularIdsProdutos(pedido);
        }
        return pedidos;
    }
}
