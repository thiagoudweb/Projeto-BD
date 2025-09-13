package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IPedidosProdutoDAO;
import com.estoqueprodutos.model.PedidosProduto;
import com.estoqueprodutos.model.PedidosProdutoId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidosProdutoDAOImpl implements IPedidosProdutoDAO {

    private final JdbcTemplate jdbcTemplate;

    public PedidosProdutoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PedidosProduto> rowMapper = (rs, rowNum) -> {
        PedidosProduto pedidosProduto = new PedidosProduto();
        PedidosProdutoId id = new PedidosProdutoId();
        id.setIdPedido(rs.getInt("id_pedido"));
        id.setIdProduto(rs.getInt("id_produto"));
        pedidosProduto.setId(id);
        pedidosProduto.setQuantidade(rs.getInt("quantidade"));
        pedidosProduto.setPrecoTotalProdutos(rs.getBigDecimal("preco_total_produtos"));
        return pedidosProduto;
    };

    @Override
    public PedidosProduto save(PedidosProduto pedidosProduto) {
        String sql = "INSERT INTO Pedidos_Produtos (id_pedido, id_produto, quantidade, preco_total_produtos) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                pedidosProduto.getIdPedido().getIdPedido(),
                pedidosProduto.getIdProduto().getIdProduto(),
                pedidosProduto.getQuantidade(),
                pedidosProduto.getPrecoTotalProdutos() != null ? pedidosProduto.getPrecoTotalProdutos() : BigDecimal.ZERO
        );
        return pedidosProduto;
    }

    @Override
    public PedidosProduto update(PedidosProduto pedidosProduto) {
        String sql = "UPDATE Pedidos_Produtos SET quantidade = ?, preco_total_produtos = ? WHERE id_pedido = ? AND id_produto = ?";
        jdbcTemplate.update(sql,
                pedidosProduto.getQuantidade(),
                pedidosProduto.getPrecoTotalProdutos(),
                pedidosProduto.getIdPedido().getIdPedido(),
                pedidosProduto.getIdProduto().getIdProduto()
        );
        return pedidosProduto;
    }

    @Override
    public void deleteById(PedidosProdutoId id) {
        String sql = "DELETE FROM Pedidos_Produtos WHERE id_pedido = ? AND id_produto = ?";
        jdbcTemplate.update(sql, id.getIdPedido(), id.getIdProduto());
    }

    @Override
    public Optional<PedidosProduto> findById(PedidosProdutoId id) {
        String sql = "SELECT * FROM Pedidos_Produtos WHERE id_pedido = ? AND id_produto = ?";
        try {
            PedidosProduto pedidosProduto = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id.getIdPedido(), id.getIdProduto()});
            return Optional.ofNullable(pedidosProduto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PedidosProduto> findAll() {
        String sql = "SELECT * FROM Pedidos_Produtos";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
