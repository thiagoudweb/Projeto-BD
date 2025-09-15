package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IEstoqueDAO;
import com.estoqueprodutos.model.Estoque;
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

@Repository
public class EstoqueDAOImpl implements IEstoqueDAO {

    private final JdbcTemplate jdbcTemplate;

    public EstoqueDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Estoque> rowMapper = (rs, rowNum) -> {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getInt("id_estoque"));
        estoque.setCodigo(rs.getString("codigo"));
        estoque.setQuantidadeExistente(rs.getInt("quantidade_existente"));

        // Set the foreign key IDs using the helper methods
        estoque.setIdProdutoId(rs.getInt("id_produto"));
        estoque.setIdArmazemId(rs.getInt("id_armazem"));

        return estoque;
    };

    @Override
    public Estoque save(Estoque estoque) {
        String sql = "INSERT INTO Estoques (id_produto, id_armazem, codigo, quantidade_existente) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, estoque.getIdProduto().getIdProduto());
            ps.setInt(2, estoque.getIdArmazem().getId());
            ps.setString(3, estoque.getCodigo());
            ps.setInt(4, estoque.getQuantidadeExistente() != null ? estoque.getQuantidadeExistente() : 0);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            estoque.setId(keyHolder.getKey().intValue());
        }
        return estoque;
    }

    @Override
    public Estoque update(Estoque estoque) {
        String sql = "UPDATE Estoques SET id_produto = ?, id_armazem = ?, codigo = ?, quantidade_existente = ? WHERE id_estoque = ?";
        jdbcTemplate.update(sql,
            estoque.getIdProduto().getIdProduto(),
            estoque.getIdArmazem().getId(),
            estoque.getCodigo(),
            estoque.getQuantidadeExistente(),
            estoque.getId());
        return estoque;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Estoques WHERE id_estoque = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Estoque> findById(Integer id) {
        String sql = "SELECT * FROM Estoques WHERE id_estoque = ?";
        try {
            Estoque estoque = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(estoque);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Estoque> findAll() {
        String sql = "SELECT * FROM Estoques";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Estoque> findByProdutoId(Integer idProduto) {
        String sql = "SELECT * FROM Estoques WHERE id_produto = ? ORDER BY quantidade_existente DESC";
        return jdbcTemplate.query(sql, rowMapper, idProduto);
    }
}
