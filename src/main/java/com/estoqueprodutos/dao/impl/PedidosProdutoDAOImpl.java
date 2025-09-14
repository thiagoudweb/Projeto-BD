package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IPedidoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.model.PedidosProduto;
import com.estoqueprodutos.model.PedidosProdutoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PedidosProdutoDAOImpl implements IPedidosProdutoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final IPedidoDAO pedidoDAO;
    private final IProdutoDAO produtoDAO;
    private final RowMapper<PedidosProduto> rowMapper;

    @Autowired
    public PedidosProdutoDAOImpl(JdbcTemplate jdbcTemplate, IPedidoDAO pedidoDAO, IProdutoDAO produtoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.pedidoDAO = pedidoDAO;
        this.produtoDAO = produtoDAO;

        this.rowMapper = (rs, rowNum) -> {
            PedidosProduto pedidosProduto = new PedidosProduto();

            // Criar o ID composto
            PedidosProdutoId id = new PedidosProdutoId();
            id.setIdPedido(rs.getInt("id_pedido"));
            id.setIdProduto(rs.getInt("id_produto"));
            pedidosProduto.setId(id);

            pedidosProduto.setQuantidade(rs.getInt("quantidade"));
            pedidosProduto.setPrecoTotalProdutos(rs.getBigDecimal("preco_total_produtos"));

            // Buscar o pedido e produto completos
            pedidoDAO.findById(rs.getInt("id_pedido")).ifPresent(pedidosProduto::setIdPedido);
            produtoDAO.findById(rs.getInt("id_produto")).ifPresent(pedidosProduto::setIdProduto);

            return pedidosProduto;
        };
    }

    @Override
    public PedidosProduto save(PedidosProduto pedidosProduto) {
        String sql = "INSERT INTO Pedidos_Produtos (id_pedido, id_produto, quantidade, preco_total_produtos) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                pedidosProduto.getId().getIdPedido(),
                pedidosProduto.getId().getIdProduto(),
                pedidosProduto.getQuantidade(),
                pedidosProduto.getPrecoTotalProdutos());

        return pedidosProduto;
    }

    @Override
    public Optional<PedidosProduto> findById(PedidosProdutoId id) {
        try {
            String sql = "SELECT * FROM Pedidos_Produtos WHERE id_pedido = ? AND id_produto = ?";
            PedidosProduto pedidosProduto = jdbcTemplate.queryForObject(sql, rowMapper, id.getIdPedido(), id.getIdProduto());
            return Optional.of(pedidosProduto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PedidosProduto> findAll() {
        String sql = "SELECT * FROM Pedidos_Produtos";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<PedidosProduto> findByPedidoId(Integer pedidoId) {
        String sql = "SELECT * FROM Pedidos_Produtos WHERE id_pedido = ?";
        return jdbcTemplate.query(sql, rowMapper, pedidoId);
    }

    @Override
    public void deleteById(PedidosProdutoId id) {
        String sql = "DELETE FROM Pedidos_Produtos WHERE id_pedido = ? AND id_produto = ?";
        jdbcTemplate.update(sql, id.getIdPedido(), id.getIdProduto());
    }
}
