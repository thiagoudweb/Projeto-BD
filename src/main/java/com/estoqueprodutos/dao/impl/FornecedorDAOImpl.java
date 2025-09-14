package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IFornecedorDAO;
import com.estoqueprodutos.model.Fornecedor;
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
public class FornecedorDAOImpl implements IFornecedorDAO {

    private final JdbcTemplate jdbcTemplate;

    public FornecedorDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Fornecedor> rowMapper = (rs, rowNum) -> {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getInt("id_fornecedor"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setTipo(rs.getString("tipo").charAt(0));
        fornecedor.setCpf(rs.getString("cpf"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setPais(rs.getString("pais"));
        return fornecedor;
    };

    @Override
    public Fornecedor save(Fornecedor fornecedor) {
        String sql = "INSERT INTO Fornecedores (nome, tipo, cpf, cnpj, pais) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTipo() != null ? fornecedor.getTipo().toString() : null);
            ps.setString(3, fornecedor.getCpf());
            ps.setString(4, fornecedor.getCnpj());
            ps.setString(5, fornecedor.getPais());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            fornecedor.setId(keyHolder.getKey().intValue());
        }
        return fornecedor;
    }

    @Override
    public Fornecedor update(Fornecedor fornecedor) {
        String sql = "UPDATE Fornecedores SET nome = ?, tipo = ?, cpf = ?, cnpj = ?, pais = ? WHERE id_fornecedor = ?";
        jdbcTemplate.update(sql,
                fornecedor.getNome(),
                fornecedor.getTipo() != null ? fornecedor.getTipo().toString() : null,
                fornecedor.getCpf(),
                fornecedor.getCnpj(),
                fornecedor.getPais(),
                fornecedor.getId());
        return fornecedor;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Fornecedores WHERE id_fornecedor = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Fornecedor> findById(Integer id) {
        String sql = "SELECT * FROM Fornecedores WHERE id_fornecedor = ?";
        try {
            Fornecedor fornecedor = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(fornecedor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Fornecedor> findAll() {
        String sql = "SELECT * FROM Fornecedores";
        return jdbcTemplate.query(sql, rowMapper);
    }
}