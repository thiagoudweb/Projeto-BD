package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IProdutosFornecedoreDAO;
import com.estoqueprodutos.model.ProdutosFornecedore;
import com.estoqueprodutos.model.ProdutosFornecedoreId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutosFornecedoreDAOImpl implements IProdutosFornecedoreDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProdutosFornecedoreDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ProdutosFornecedore> rowMapper = (rs, rowNum) -> {
        ProdutosFornecedore produtosFornecedore = new ProdutosFornecedore();
        ProdutosFornecedoreId id = new ProdutosFornecedoreId();
        id.setIdProduto(rs.getInt("id_produto"));
        id.setIdFornecedor(rs.getInt("id_fornecedor"));
        produtosFornecedore.setId(id);
        return produtosFornecedore;
    };

    @Override
    public ProdutosFornecedore save(ProdutosFornecedore produtosFornecedore) {
        String sql = "INSERT INTO Produtos_Fornecedores (id_produto, id_fornecedor) VALUES (?, ?)";
        jdbcTemplate.update(sql,
            produtosFornecedore.getIdProduto().getIdProduto(),
            produtosFornecedore.getIdFornecedor().getId()
        );
        return produtosFornecedore;
    }

    @Override
    public ProdutosFornecedore update(ProdutosFornecedore produtosFornecedore) {
        String sql = "UPDATE Produtos_Fornecedores SET id_produto = ?, id_fornecedor = ? WHERE id_produto = ? AND id_fornecedor = ?";
        jdbcTemplate.update(sql,
            produtosFornecedore.getIdProduto().getIdProduto(),
            produtosFornecedore.getIdFornecedor().getId(),
            produtosFornecedore.getId().getIdProduto(),
            produtosFornecedore.getId().getIdFornecedor()
        );
        return produtosFornecedore;
    }

    @Override
    public void deleteById(ProdutosFornecedoreId id) {
        String sql = "DELETE FROM Produtos_Fornecedores WHERE id_produto = ? AND id_fornecedor = ?";
        jdbcTemplate.update(sql, id.getIdProduto(), id.getIdFornecedor());
    }

    @Override
    public Optional<ProdutosFornecedore> findById(ProdutosFornecedoreId id) {
        String sql = "SELECT * FROM Produtos_Fornecedores WHERE id_produto = ? AND id_fornecedor = ?";
        try {
            ProdutosFornecedore produtosFornecedore = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id.getIdProduto(), id.getIdFornecedor()});
            return Optional.ofNullable(produtosFornecedore);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProdutosFornecedore> findAll() {
        String sql = "SELECT * FROM Produtos_Fornecedores";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
