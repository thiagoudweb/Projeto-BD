package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Cliente.
 */
public interface IClienteDAO {

    /**
     * Salva um novo cliente no banco de dados.
     * @param cliente O objeto cliente a ser salvo.
     * @return O cliente salvo com o ID gerado.
     */
    Cliente save(Cliente cliente);

    /**
     * Atualiza um cliente existente no banco de dados.
     * @param cliente O objeto cliente com os dados atualizados.
     * @return O cliente atualizado.
     */
    Cliente update(Cliente cliente);

    /**
     * Deleta um cliente do banco de dados pelo seu ID.
     * @param id O ID do cliente a ser deletado.
     */
    void deleteById(Integer id);

    /**
     * Busca um cliente pelo seu ID.
     * @param id O ID do cliente a ser buscado.
     * @return um Optional contendo o cliente, se encontrado.
     */
    Optional<Cliente> findById(Integer id);

    /**
     * Lista todos os clientes cadastrados.
     * @return Uma lista com todos os clientes.
     */
    List<Cliente> findAll();
}
