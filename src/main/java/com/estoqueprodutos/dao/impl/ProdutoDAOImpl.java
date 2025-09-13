package com.estoqueprodutos.dao.impl;

import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoIdiomaDAO;
import com.estoqueprodutos.model.Produto;
import com.estoqueprodutos.model.ProdutoIdioma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoDAOImpl implements IProdutoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final IProdutoIdiomaDAO produtoIdiomaDAO;

    @Autowired
    public ProdutoDAOImpl(JdbcTemplate jdbcTemplate, IProdutoIdiomaDAO produtoIdiomaDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtoIdiomaDAO = produtoIdiomaDAO;
    }

    private final RowMapper<Produto> rowMapper = (rs, rowNum) -> {
        Produto produto = new Produto();
        produto.setIdProduto(rs.getInt("id_produto"));
        produto.setDataGarantia(rs.getDate("data_garantia") != null ?
            rs.getDate("data_garantia").toLocalDate() : null);
        produto.setStatus(rs.getString("status"));
        produto.setPrecoProduto(rs.getBigDecimal("preco_produto"));
        produto.setPrecoVendaMinimo(rs.getBigDecimal("preco_venda_minimo"));
        return produto;
    };

    @Override
    public Produto save(Produto produto) {
        String sql = "INSERT INTO Produtos (data_garantia, status, preco_produto, preco_venda_minimo) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (produto.getDataGarantia() != null) {
                ps.setDate(1, java.sql.Date.valueOf(produto.getDataGarantia()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            ps.setString(2, produto.getStatus());
            ps.setBigDecimal(3, produto.getPrecoProduto());
            ps.setBigDecimal(4, produto.getPrecoVendaMinimo());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            produto.setIdProduto(keyHolder.getKey().intValue());
        }

        // Salvar idiomas se existirem
        if (produto.getIdiomas() != null && !produto.getIdiomas().isEmpty()) {
            for (ProdutoIdioma idioma : produto.getIdiomas()) {
                idioma.setProduto(produto);
                produtoIdiomaDAO.save(idioma);
            }
        }

        return produto;
    }

    @Override
    public Produto update(Produto produto) {
        if (produto.getIdProduto() == null) {
            throw new IllegalArgumentException("ID deve ser not null");
        }
        String sql = "UPDATE Produtos SET data_garantia = ?, status = ?, preco_produto = ?, preco_venda_minimo = ? WHERE id_produto = ?";
        jdbcTemplate.update(sql,
                produto.getDataGarantia() != null ? java.sql.Date.valueOf(produto.getDataGarantia()) : null,
                produto.getStatus(),
                produto.getPrecoProduto(),
                produto.getPrecoVendaMinimo(),
                produto.getIdProduto());

        // Atualizar idiomas - primeiro remover os existentes, depois adicionar os novos
        produtoIdiomaDAO.deleteByProdutoId(produto.getIdProduto());
        if (produto.getIdiomas() != null && !produto.getIdiomas().isEmpty()) {
            for (ProdutoIdioma idioma : produto.getIdiomas()) {
                idioma.setProduto(produto);
                produtoIdiomaDAO.save(idioma);
            }
        }

        return produto;
    }

    @Override
    public void deleteById(Integer id) {
        // Primeiro deletar idiomas relacionados
        produtoIdiomaDAO.deleteByProdutoId(id);
        // Depois deletar o produto
        String sql = "DELETE FROM Produtos WHERE id_produto = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Produto> findById(Integer id) {
        String sql = "SELECT * FROM Produtos WHERE id_produto = ?";
        try {
            Produto produto = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{id});
            if (produto != null) {
                // Carregar idiomas (sem setar back-reference para evitar ciclos no JSON)
                List<ProdutoIdioma> idiomas = produtoIdiomaDAO.findByProdutoId(id);
                produto.setIdiomas(idiomas);
            }
            return Optional.ofNullable(produto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Produto> findAll() {
        String sql = "SELECT * FROM Produtos";
        List<Produto> produtos = jdbcTemplate.query(sql, rowMapper);

        // Carregar idiomas para cada produto (sem setar back-reference)
        for (Produto produto : produtos) {
            List<ProdutoIdioma> idiomas = produtoIdiomaDAO.findByProdutoId(produto.getIdProduto());
            produto.setIdiomas(idiomas);
        }

        return produtos;
    }
}
