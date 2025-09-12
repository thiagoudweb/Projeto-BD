package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IClienteDAO;
import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.model.Cliente;
import com.estoqueprodutos.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoDAOImpl implements IPedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final IClienteDAO clienteDAO; // Para buscar o objeto Cliente completo
    private final RowMapper<Pedido> rowMapper;

    @Autowired
    public PedidoDAOImpl(JdbcTemplate jdbcTemplate, IClienteDAO clienteDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.clienteDAO = clienteDAO;
        this.rowMapper = (rs, rowNum) -> {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("id_pedido"));
            pedido.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
            if (rs.getDate("prazo_entrega") != null) {
                pedido.setPrazoEntrega(rs.getDate("prazo_entrega").toLocalDate());
            }
            pedido.setPrecoFinal(rs.getBigDecimal("preco_final"));
            pedido.setStatus(rs.getString("status"));
            pedido.setModoEncomenda(rs.getString("modo_encomenda"));

            int clienteId = rs.getInt("id_cliente");
            // Agora o acesso a 'this.clienteDAO' é seguro
            this.clienteDAO.findById(clienteId).ifPresent(pedido::setCliente);

            return pedido;
        };
    }


    @Override
    public Pedido save(Pedido pedido) {
        // Validação básica para garantir que o cliente existe e tem ID
        if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("Pedido deve estar associado a um cliente com ID válido.");
        }

        String sql = "INSERT INTO Pedidos (data_pedido, prazo_entrega, preco_final, status, modo_encomenda, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(pedido.getDataPedido()));
            ps.setDate(2, pedido.getPrazoEntrega() != null ? java.sql.Date.valueOf(pedido.getPrazoEntrega()) : null);
            ps.setBigDecimal(3, pedido.getPrecoFinal());
            ps.setString(4, pedido.getStatus());
            ps.setString(5, pedido.getModoEncomenda());
            ps.setInt(6, pedido.getCliente().getIdCliente());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            pedido.setIdPedido(keyHolder.getKey().intValue());
        }
        return pedido;
    }


    @Override
    public Optional<Pedido> findById(Integer id) {
        String sql = "SELECT * FROM Pedidos WHERE id_pedido = ?";
        try {
            Pedido pedido = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
            return Optional.ofNullable(pedido);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT * FROM Pedidos";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Pedidos WHERE id_pedido = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Pedido> findByClienteId(Integer clienteId) {
        String sql = "SELECT * FROM Pedidos WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, new Object[]{clienteId}, rowMapper);
    }
}
