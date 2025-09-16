package com.estoqueprodutos.dao.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.estoqueprodutos.dao.interfaces.IArmazenDAO;
import com.estoqueprodutos.model.Armazen;
import org.springframework.stereotype.Repository;

@Repository
public class ArmazenDAOImpl implements IArmazenDAO{
    
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Armazen> rowMapper;

    public ArmazenDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rs, rowNum) -> {
            Armazen armazen = new Armazen();
            armazen.setId(rs.getInt("id_armazem"));
            armazen.setNome(rs.getString("nome"));
            return armazen;
        };
    }

    @Override
    public Armazen save(Armazen armazem){
        String sql = "INSERT INTO Armazens (nome) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, armazem.getNome());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            armazem.setId(keyHolder.getKey().intValue());
        }
        return armazem;
    }
    
    @Override
    public Armazen update(Armazen armazem) {
        if (armazem.getId() == null) {
            throw new IllegalArgumentException("ID deve ser not null");
        }

        String sql = "UPDATE Armazens SET nome = ? WHERE id_armazem = ?";
        jdbcTemplate.update(sql, armazem.getNome(), armazem.getId());
        return armazem;
    }

    @Override
    public Optional<Armazen> findById(Integer id) {
        String sql = "SELECT * FROM Armazens WHERE id_armazem = ?";
        try {
            Armazen armazem = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(armazem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Armazen> findAll() {
        String sql = "SELECT * FROM Armazens";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Armazens WHERE id_armazem = ?";
        jdbcTemplate.update(sql, id);
    }
}
