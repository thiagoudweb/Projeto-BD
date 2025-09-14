package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IFornecedoreDAO;
import com.estoqueprodutos.model.Fornecedore;
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
public class FornecedoreDAOImpl implements IFornecedoreDAO {

    private final JdbcTemplate jdbcTemplate;

    public FornecedoreDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Fornecedore> rowMapper = (rs, rowNum) -> {
        Fornecedore fornecedore = new Fornecedore();
        fornecedore.setId(rs.getInt("id_fornecedor"));
        fornecedore.setNome(rs.getString("nome"));
        fornecedore.setTipo(rs.getString("tipo").charAt(0));
        fornecedore.setCpf(rs.getString("cpf"));
        fornecedore.setCnpj(rs.getString("cnpj"));
        fornecedore.setPais(rs.getString("pais"));
        return fornecedore;
    };

    @Override
    public Fornecedore save(Fornecedore fornecedore) {
        String sql = "INSERT INTO Fornecedores (nome, tipo, cpf, cnpj, pais) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fornecedore.getNome());
            ps.setString(2, fornecedore.getTipo() != null ? fornecedore.getTipo().toString() : null);
            ps.setString(3, fornecedore.getCpf());
            ps.setString(4, fornecedore.getCnpj());
            ps.setString(5, fornecedore.getPais());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            fornecedore.setId(keyHolder.getKey().intValue());
        }
        return fornecedore;
    }

    @Override
    public Fornecedore update(Fornecedore fornecedore) {
        String sql = "UPDATE Fornecedores SET nome = ?, tipo = ?, cpf = ?, cnpj = ?, pais = ? WHERE id_fornecedor = ?";
        jdbcTemplate.update(sql,
            fornecedore.getNome(),
            fornecedore.getTipo() != null ? fornecedore.getTipo().toString() : null,
            fornecedore.getCpf(),
            fornecedore.getCnpj(),
            fornecedore.getPais(),
            fornecedore.getId());
        return fornecedore;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Fornecedores WHERE id_fornecedor = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Fornecedore> findById(Integer id) {
        String sql = "SELECT * FROM Fornecedores WHERE id_fornecedor = ?";
        try {
            Fornecedore fornecedore = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(fornecedore);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Fornecedore> findAll() {
        String sql = "SELECT * FROM Fornecedores";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
