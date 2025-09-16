package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.ICategoriaDAO;
import com.estoqueprodutos.model.Categoria;
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
public class CategoriaDAOImpl implements ICategoriaDAO {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Categoria> rowMapper;

    public CategoriaDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rs, rowNum) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNome(rs.getString("nome"));
            categoria.setDescricao(rs.getString("descricao")); // LOB tratado como String
            return categoria;
        };
    }

    @Override
    public Categoria save(Categoria categoria) {
        String sql = "INSERT INTO Categorias (nome, descricao) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getDescricao());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            categoria.setId(keyHolder.getKey().intValue());
        }
        return categoria;
    }

    @Override
    public Categoria update(Categoria categoria) {
        if (categoria.getId() == null) {
            throw new IllegalArgumentException("ID é not null");
        }
        String sql = "UPDATE Categorias SET nome = ?, descricao = ? WHERE id_categoria = ?";
        jdbcTemplate.update(sql, categoria.getNome(), categoria.getDescricao(), categoria.getId());
        return categoria;
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        String sql = "SELECT * FROM Categorias WHERE id_categoria = ?";
        try {
            Categoria categoria = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(categoria);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT * FROM Categorias";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Categorias WHERE id_categoria = ?";
        jdbcTemplate.update(sql, id);
    }
}
