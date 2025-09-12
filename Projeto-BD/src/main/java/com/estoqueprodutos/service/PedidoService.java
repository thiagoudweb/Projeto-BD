package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.model.Pedido;
import com.estoqueprodutos.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final IPedidoDAO pedidoDAO;
    private final IClienteDAO clienteDAO;

    @Autowired
    public PedidoService(IPedidoDAO pedidoDAO, IClienteDAO clienteDAO) {
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
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

        return pedidoDAO.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoDAO.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoDAO.findById(id);
    }

    public List<Pedido> buscarPorClienteId(Integer clienteId) {
        return pedidoDAO.findByClienteId(clienteId);
    }

    @Transactional
    public void deletar(Integer id) {
        pedidoDAO.deleteById(id);
    }
}
