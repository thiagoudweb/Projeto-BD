package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IEnderecoDAO;
import com.estoqueprodutos.model.Endereco;
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
public class EnderecoDAOImpl implements IEnderecoDAO {

    private final JdbcTemplate jdbcTemplate;

    public EnderecoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Endereco> rowMapper = (rs, rowNum) -> {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("id_endereco"));
        endereco.setPais(rs.getString("pais"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setRua(rs.getString("rua"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setCep(rs.getString("cep"));
        endereco.setComplemento(rs.getString("complemento"));
        return endereco;
    };

    @Override
    public Endereco save(Endereco endereco) {
        String sql = "INSERT INTO Enderecos (pais, estado, cidade, bairro, rua, numero, cep, complemento, id_cliente, id_entregadora, id_fornecedor, id_armazem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, endereco.getPais());
            ps.setString(2, endereco.getEstado());
            ps.setString(3, endereco.getCidade());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getRua());
            ps.setString(6, endereco.getNumero());
            ps.setString(7, endereco.getCep());
            ps.setString(8, endereco.getComplemento());
            ps.setObject(9, endereco.getIdCliente() != null ? endereco.getIdCliente().getIdCliente() : null);
            ps.setObject(10, endereco.getIdEntregadora() != null ? endereco.getIdEntregadora().getId() : null);
            ps.setObject(11, endereco.getIdFornecedor() != null ? endereco.getIdFornecedor().getId() : null);
            ps.setObject(12, endereco.getIdArmazem() != null ? endereco.getIdArmazem().getId() : null);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            endereco.setId(keyHolder.getKey().intValue());
        }
        return endereco;
    }

    @Override
    public Endereco update(Endereco endereco) {
        String sql = "UPDATE Enderecos SET pais = ?, estado = ?, cidade = ?, bairro = ?, rua = ?, numero = ?, cep = ?, complemento = ?, id_cliente = ?, id_entregadora = ?, id_fornecedor = ?, id_armazem = ? WHERE id_endereco = ?";
        jdbcTemplate.update(sql,
                endereco.getPais(),
                endereco.getEstado(),
                endereco.getCidade(),
                endereco.getBairro(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCep(),
                endereco.getComplemento(),
                endereco.getIdCliente() != null ? endereco.getIdCliente().getIdCliente() : null,
                endereco.getIdEntregadora() != null ? endereco.getIdEntregadora().getId() : null,
                endereco.getIdFornecedor() != null ? endereco.getIdFornecedor().getId() : null,
                endereco.getIdArmazem() != null ? endereco.getIdArmazem().getId() : null,
                endereco.getId());
        return endereco;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Enderecos WHERE id_endereco = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Endereco> findById(Integer id) {
        String sql = "SELECT * FROM Enderecos WHERE id_endereco = ?";
        try {
            Endereco endereco = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            return Optional.ofNullable(endereco);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Endereco> findAll() {
        String sql = "SELECT * FROM Enderecos";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
