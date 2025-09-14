package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IProdutosFornecedoresDAO;
import com.estoqueprodutos.model.ProdutosFornecedores;
import com.estoqueprodutos.model.ProdutosFornecedoresId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutosFornecedoresDAOImpl implements IProdutosFornecedoresDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProdutosFornecedoresDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ProdutosFornecedores> rowMapper = (rs, rowNum) -> {
        ProdutosFornecedores produtosFornecedores = new ProdutosFornecedores();
        ProdutosFornecedoresId id = new ProdutosFornecedoresId();
        id.setIdProduto(rs.getInt("id_produto"));
        id.setIdFornecedor(rs.getInt("id_fornecedor"));
        produtosFornecedores.setId(id);
        return produtosFornecedores;
    };

    @Override
    public ProdutosFornecedores save(ProdutosFornecedores produtosFornecedores) {
        String sql = "INSERT INTO Produtos_Fornecedores (id_produto, id_fornecedor) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                produtosFornecedores.getIdProduto().getIdProduto(),
                produtosFornecedores.getIdFornecedor().getId()
        );
        return produtosFornecedores;
    }

    @Override
    public ProdutosFornecedores update(ProdutosFornecedores produtosFornecedores) {
        String sql = "UPDATE Produtos_Fornecedores SET id_produto = ?, id_fornecedor = ? WHERE id_produto = ? AND id_fornecedor = ?";
        jdbcTemplate.update(sql,
                produtosFornecedores.getIdProduto().getIdProduto(),
                produtosFornecedores.getIdFornecedor().getId(),
                produtosFornecedores.getId().getIdProduto(),
                produtosFornecedores.getId().getIdFornecedor()
        );
        return produtosFornecedores;
    }

    @Override
    public void deleteById(ProdutosFornecedoresId id) {
        String sql = "DELETE FROM Produtos_Fornecedores WHERE id_produto = ? AND id_fornecedor = ?";
        jdbcTemplate.update(sql, id.getIdProduto(), id.getIdFornecedor());
    }

    @Override
    public Optional<ProdutosFornecedores> findById(ProdutosFornecedoresId id) {
        String sql = "SELECT * FROM Produtos_Fornecedores WHERE id_produto = ? AND id_fornecedor = ?";
        try {
            ProdutosFornecedores produtosFornecedores = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id.getIdProduto(), id.getIdFornecedor()});
            return Optional.ofNullable(produtosFornecedores);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProdutosFornecedores> findAll() {
        String sql = "SELECT * FROM Produtos_Fornecedores";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Métodos específicos para fornecedor
    @Override
    public List<ProdutosFornecedores> findByFornecedorId(Integer fornecedorId) {
        String sql = "SELECT * FROM Produtos_Fornecedores WHERE id_fornecedor = ?";
        return jdbcTemplate.query(sql, rowMapper, fornecedorId);
    }

    @Override
    public void deleteByFornecedorIdAndProdutoId(Integer fornecedorId, Integer produtoId) {
        String sql = "DELETE FROM Produtos_Fornecedores WHERE id_fornecedor = ? AND id_produto = ?";
        jdbcTemplate.update(sql, fornecedorId, produtoId);
    }
}