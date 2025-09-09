package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClienteDAOImpl implements IClienteDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // O RowMapper agora mapeia apenas os campos da tabela 'Clientes'.
    // Os emails e telefones são carregados separadamente.
    private final RowMapper<Cliente> rowMapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setLimiteCredito(rs.getBigDecimal("limite_credito"));
        cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
        return cliente;
    };

    @Override
    public Cliente save(Cliente cliente) {
        // O SQL agora não inclui a coluna 'email'
        String sql = "INSERT INTO Clientes (nome, limite_credito, data_cadastro) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setBigDecimal(2, cliente.getLimiteCredito());
            ps.setDate(3, java.sql.Date.valueOf(cliente.getDataCadastro()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cliente.setIdCliente(keyHolder.getKey().intValue());
            // Após salvar o cliente e obter o ID, salvamos os emails e telefones
            salvarEmailsETelefones(cliente);
        }
        return cliente;
    }

    @Override
    public Cliente update(Cliente cliente) {
        String sql = "UPDATE Clientes SET nome = ?, limite_credito = ?, data_cadastro = ? WHERE id_cliente = ?";
        jdbcTemplate.update(sql,
                cliente.getNome(),
                cliente.getLimiteCredito(),
                cliente.getDataCadastro(),
                cliente.getIdCliente());

        // Deleta os emails/telefones antigos e insere os novos para garantir consistência
        deletarEmailsETelefones(cliente.getIdCliente());
        salvarEmailsETelefones(cliente);

        return cliente;
    }

    @Override
    public void deleteById(Integer id) {
        // Primeiro deleta os registros dependentes
        deletarEmailsETelefones(id);
        // Depois deleta o cliente principal
        String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
            if (cliente != null) {
                // Após encontrar o cliente, carregamos suas listas
                carregarEmailsETelefones(cliente);
            }
            return Optional.ofNullable(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT * FROM Clientes";
        List<Cliente> clientes = jdbcTemplate.query(sql, rowMapper);
        // Para cada cliente na lista, carrega suas informações de contato
        return clientes.stream()
                .peek(this::carregarEmailsETelefones)
                .collect(Collectors.toList());
    }

    private void salvarEmailsETelefones(Cliente cliente) {
        String sqlEmail = "INSERT INTO Clientes_Emails (id_cliente, email) VALUES (?, ?)";
        cliente.getEmails().forEach(email -> jdbcTemplate.update(sqlEmail, cliente.getIdCliente(), email));

        String sqlTelefone = "INSERT INTO Clientes_Telefones (id_cliente, telefone) VALUES (?, ?)";
        cliente.getTelefones().forEach(telefone -> jdbcTemplate.update(sqlTelefone, cliente.getIdCliente(), telefone));
    }

    private void deletarEmailsETelefones(Integer idCliente) {
        jdbcTemplate.update("DELETE FROM Clientes_Emails WHERE id_cliente = ?", idCliente);
        jdbcTemplate.update("DELETE FROM Clientes_Telefones WHERE id_cliente = ?", idCliente);
    }

    private void carregarEmailsETelefones(Cliente cliente) {
        String sqlEmail = "SELECT email FROM Clientes_Emails WHERE id_cliente = ?";
        List<String> emails = jdbcTemplate.queryForList(sqlEmail, String.class, cliente.getIdCliente());
        cliente.setEmails(emails);

        String sqlTelefone = "SELECT telefone FROM Clientes_Telefones WHERE id_cliente = ?";
        List<String> telefones = jdbcTemplate.queryForList(sqlTelefone, String.class, cliente.getIdCliente());
        cliente.setTelefones(telefones);
    }
}

