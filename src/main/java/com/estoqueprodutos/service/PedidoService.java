package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final IPedidoDAO pedidoDAO;

    @Autowired
    public PedidoService(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
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
