package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IProdutosCategoriaDAO;
import com.estoqueprodutos.model.ProdutosCategoria;
import com.estoqueprodutos.model.ProdutosCategoriaId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutosCategoriaDAOImpl implements IProdutosCategoriaDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProdutosCategoriaDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ProdutosCategoria> rowMapper = (rs, rowNum) -> {
        ProdutosCategoria produtosCategoria = new ProdutosCategoria();
        ProdutosCategoriaId id = new ProdutosCategoriaId();
        id.setIdProduto(rs.getInt("id_produto"));
        id.setIdCategoria(rs.getInt("id_categoria"));
        produtosCategoria.setId(id);
        return produtosCategoria;
    };

    @Override
    public ProdutosCategoria save(ProdutosCategoria produtosCategoria) {
        String sql = "INSERT INTO Produtos_Categorias (id_produto, id_categoria) VALUES (?, ?)";
        jdbcTemplate.update(sql,
            produtosCategoria.getIdProduto().getIdProduto(),
            produtosCategoria.getIdCategoria().getId()
        );
        return produtosCategoria;
    }

    @Override
    public ProdutosCategoria update(ProdutosCategoria produtosCategoria) {
        String sql = "UPDATE Produtos_Categorias SET id_produto = ?, id_categoria = ? WHERE id_produto = ? AND id_categoria = ?";
        jdbcTemplate.update(sql,
            produtosCategoria.getIdProduto().getIdProduto(),
            produtosCategoria.getIdCategoria().getId(),
            produtosCategoria.getId().getIdProduto(),
            produtosCategoria.getId().getIdCategoria()
        );
        return produtosCategoria;
    }

    @Override
    public void deleteById(ProdutosCategoriaId id) {
        String sql = "DELETE FROM Produtos_Categorias WHERE id_produto = ? AND id_categoria = ?";
        jdbcTemplate.update(sql, id.getIdProduto(), id.getIdCategoria());
    }

    @Override
    public Optional<ProdutosCategoria> findById(ProdutosCategoriaId id) {
        String sql = "SELECT * FROM Produtos_Categorias WHERE id_produto = ? AND id_categoria = ?";
        try {
            ProdutosCategoria produtosCategoria = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id.getIdProduto(), id.getIdCategoria()});
            return Optional.ofNullable(produtosCategoria);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProdutosCategoria> findAll() {
        String sql = "SELECT * FROM Produtos_Categorias";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
