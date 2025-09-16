package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IEntregadorasTelefoneDAO;
import com.estoqueprodutos.model.EntregadorasTelefone;
import com.estoqueprodutos.model.EntregadorasTelefoneId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EntregadorasTelefoneDAOImpl implements IEntregadorasTelefoneDAO {

    private final JdbcTemplate jdbcTemplate;

    public EntregadorasTelefoneDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EntregadorasTelefone> rowMapper = (rs, rowNum) -> {
        EntregadorasTelefone et = new EntregadorasTelefone();
        EntregadorasTelefoneId id = new EntregadorasTelefoneId();
        id.setIdEntregadora(rs.getInt("id_entregadora"));
        id.setTelefone(rs.getString("telefone"));
        et.setId(id);
        return et;
    };

    @Override
    public EntregadorasTelefone save(EntregadorasTelefone entregadorasTelefone) {
        String sql = "INSERT INTO Entregadoras_Telefones (id_entregadora, telefone) VALUES (?, ?)";
        jdbcTemplate.update(sql,
            entregadorasTelefone.getIdEntregadora().getId(),
            entregadorasTelefone.getId().getTelefone());
        return entregadorasTelefone;
    }

    @Override
    public EntregadorasTelefone update(EntregadorasTelefone entregadorasTelefone) {
        String sql = "UPDATE Entregadoras_Telefones SET telefone = ? WHERE id_entregadora = ? AND telefone = ?";
        jdbcTemplate.update(sql,
            entregadorasTelefone.getId().getTelefone(),
            entregadorasTelefone.getIdEntregadora().getId(),
            entregadorasTelefone.getId().getTelefone());
        return entregadorasTelefone;
    }

    @Override
    public void deleteById(EntregadorasTelefoneId id) {
        String sql = "DELETE FROM Entregadoras_Telefones WHERE id_entregadora = ? AND telefone = ?";
        jdbcTemplate.update(sql, id.getIdEntregadora(), id.getTelefone());
    }

    @Override
    public Optional<EntregadorasTelefone> findById(EntregadorasTelefoneId id) {
        String sql = "SELECT * FROM Entregadoras_Telefones WHERE id_entregadora = ? AND telefone = ?";
        try {
            EntregadorasTelefone et = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id.getIdEntregadora(), id.getTelefone()});
            return Optional.ofNullable(et);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<EntregadorasTelefone> findAll() {
        String sql = "SELECT * FROM Entregadoras_Telefones";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
