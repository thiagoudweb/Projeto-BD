package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta classe como um componente de serviço (lógica de negócio)
public class ClienteService {

    private final IClienteDAO clienteDAO;

    // Injeção de dependência da interface DAO via construtor
    @Autowired
    public ClienteService(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /**
     * Orquestra a lógica para salvar ou atualizar um cliente.
     * @param cliente o cliente a ser salvo
     * @return o cliente salvo
     */
    public Cliente salvar(Cliente cliente) {
        // A lógica do controller já diferencia a criação da atualização,
        // mas o serviço poderia ter uma lógica mais complexa aqui se necessário.
        // Se o ID for nulo, é um novo cliente. Se não, é uma atualização.
        if (cliente.getIdCliente() == null) {
            return clienteDAO.save(cliente);
        } else {
            return clienteDAO.update(cliente);
        }
    }

    /**
     * Busca todos os clientes.
     * @return lista de clientes
     */
    public List<Cliente> listarTodos() {
        return clienteDAO.findAll();
    }

    /**
     * Busca um cliente por ID.
     * @param id o ID do cliente
     * @return um Optional contendo o cliente, se encontrado
     */
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteDAO.findById(id);
    }

    /**
     * Deleta um cliente por ID.
     * @param id o ID do cliente a ser deletado
     */
    public void deletar(Integer id) {
        clienteDAO.deleteById(id);
    }
}
