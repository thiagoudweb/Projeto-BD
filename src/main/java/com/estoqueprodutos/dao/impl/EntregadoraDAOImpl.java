package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IEntregadoraDAO;
import com.estoqueprodutos.model.Entregadora;
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
public class EntregadoraDAOImpl implements IEntregadoraDAO {

    private final JdbcTemplate jdbcTemplate;

    public EntregadoraDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Entregadora> rowMapper = (rs, rowNum) -> {
        Entregadora entregadora = new Entregadora();
        entregadora.setId(rs.getInt("id_entregadora"));
        entregadora.setNome(rs.getString("nome"));
        entregadora.setNomeFantasia(rs.getString("nome_fantasia"));
        entregadora.setCnpj(rs.getString("cnpj"));
        entregadora.setEmail(rs.getString("email"));
        return entregadora;
    };

    @Override
    public Entregadora save(Entregadora entregadora) {
        String sql = "INSERT INTO Entregadoras (nome, nome_fantasia, cnpj, email) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entregadora.getNome());
            ps.setString(2, entregadora.getNomeFantasia());
            ps.setString(3, entregadora.getCnpj());
            ps.setString(4, entregadora.getEmail());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            entregadora.setId(keyHolder.getKey().intValue());
        }
        return entregadora;
    }

    @Override
    public Entregadora update(Entregadora entregadora) {
        String sql = "UPDATE Entregadoras SET nome = ?, nome_fantasia = ?, cnpj = ?, email = ? WHERE id_entregadora = ?";
        jdbcTemplate.update(sql,
                entregadora.getNome(),
                entregadora.getNomeFantasia(),
                entregadora.getCnpj(),
                entregadora.getEmail(),
                entregadora.getId());
        return entregadora;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Entregadoras WHERE id_entregadora = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Entregadora> findById(Integer id) {
        String sql = "SELECT * FROM Entregadoras WHERE id_entregadora = ?";
        try {
            Entregadora entregadora = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(entregadora);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Entregadora> findAll() {
        String sql = "SELECT * FROM Entregadoras";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
