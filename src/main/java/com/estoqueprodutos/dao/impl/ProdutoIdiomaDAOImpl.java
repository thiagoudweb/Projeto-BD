package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IProdutoIdiomaDAO;
import com.estoqueprodutos.model.ProdutoIdioma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoIdiomaDAOImpl implements IProdutoIdiomaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProdutoIdiomaDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ProdutoIdioma> rowMapper = (rs, rowNum) -> {
        ProdutoIdioma produtoIdioma = new ProdutoIdioma();
        produtoIdioma.setIdioma(rs.getString("idioma"));
        produtoIdioma.setNomeTraduzido(rs.getString("nome_traduzido"));
        produtoIdioma.setDescricaoTraduzida(rs.getString("descricao_traduzida"));
        // Note: produto será carregado separadamente para evitar referência circular
        return produtoIdioma;
    };

    @Override
    public ProdutoIdioma save(ProdutoIdioma produtoIdioma) {
        // Use SCOPE_IDENTITY to fetch the generated identity value on SQL Server without relying on column name
        String sql = "INSERT INTO Produtos_Idiomas (id_produto, idioma, nome_traduzido, descricao_traduzida) VALUES (?, ?, ?, ?); " +
                     "SELECT CAST(SCOPE_IDENTITY() AS int)";

        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                produtoIdioma.getProduto().getIdProduto(),
                produtoIdioma.getIdioma(),
                produtoIdioma.getNomeTraduzido(),
                produtoIdioma.getDescricaoTraduzida()
        );

        if (generatedId != null) {
            produtoIdioma.setId(generatedId);
        }
        return produtoIdioma;
    }

    @Override
    public ProdutoIdioma update(ProdutoIdioma produtoIdioma) {
        String sql = "UPDATE Produtos_Idiomas SET idioma = ?, nome_traduzido = ?, descricao_traduzida = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                produtoIdioma.getIdioma(),
                produtoIdioma.getNomeTraduzido(),
                produtoIdioma.getDescricaoTraduzida(),
                produtoIdioma.getId());
        return produtoIdioma;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Produtos_Idiomas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteByProdutoId(Integer produtoId) {
        String sql = "DELETE FROM Produtos_Idiomas WHERE id_produto = ?";
        jdbcTemplate.update(sql, produtoId);
    }

    @Override
    public Optional<ProdutoIdioma> findById(Integer id) {
        String sql = "SELECT * FROM Produtos_Idiomas WHERE id = ?";
        try {
            ProdutoIdioma produtoIdioma = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
            return Optional.ofNullable(produtoIdioma);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProdutoIdioma> findAll() {
        String sql = "SELECT * FROM Produtos_Idiomas";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ProdutoIdioma> findByProdutoId(Integer produtoId) {
        String sql = "SELECT * FROM Produtos_Idiomas WHERE id_produto = ?";
        return jdbcTemplate.query(sql, new Object[]{produtoId}, rowMapper);
    }
}
