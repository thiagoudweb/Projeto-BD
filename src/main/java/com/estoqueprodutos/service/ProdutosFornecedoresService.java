package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.IProdutosFornecedoresDAO;
import com.estoqueprodutos.dao.interfaces.IFornecedorDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.dao.interfaces.IProdutoIdiomaDAO;
import com.estoqueprodutos.model.ProdutosFornecedores;
import com.estoqueprodutos.model.ProdutosFornecedoresId;
import com.estoqueprodutos.model.Fornecedor;
import com.estoqueprodutos.model.Produto;
import com.estoqueprodutos.model.ProdutoIdioma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutosFornecedoresService {

    @Autowired
    private IProdutosFornecedoresDAO produtosFornecedoresDAO;

    @Autowired
    private IFornecedorDAO fornecedorDAO;

    @Autowired
    private IProdutoDAO produtoDAO;

    @Autowired
    private IProdutoIdiomaDAO produtoIdiomaDAO;

    // Buscar produtos por fornecedor - retorna apenas IDs
    public List<Integer> buscarProdutoIdsPorFornecedor(Integer fornecedorId) {
        return produtosFornecedoresDAO.findByFornecedorId(fornecedorId)
                .stream()
                .map(pf -> pf.getId().getIdProduto())
                .collect(Collectors.toList());
    }

    // Buscar produtos completos por fornecedor
    public List<Map<String, Object>> buscarProdutosCompletosPorFornecedor(Integer fornecedorId) {
        List<ProdutosFornecedores> associacoes = produtosFornecedoresDAO.findByFornecedorId(fornecedorId);

        return associacoes.stream()
                .map(pf -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("fornecedorId", pf.getId().getIdFornecedor());
                    resultado.put("produtoId", pf.getId().getIdProduto());

                    // Buscar dados do produto
                    produtoDAO.findById(pf.getId().getIdProduto()).ifPresent(produto -> {
                        resultado.put("precoProduto", produto.getPrecoProduto());
                        resultado.put("status", produto.getStatus());
                        // Buscar nome do produto em ProdutoIdioma (pegar o primeiro idioma disponível)
                        List<ProdutoIdioma> idiomas = produtoIdiomaDAO.findByProdutoId(produto.getIdProduto());
                        if (!idiomas.isEmpty()) {
                            ProdutoIdioma produtoIdioma = idiomas.get(0); // Pega o primeiro idioma
                            resultado.put("nomeProduto", produtoIdioma.getNomeTraduzido());
                        }
                    });

                    return resultado;
                })
                .collect(Collectors.toList());
    }

    // Versão original para compatibilidade
    public List<ProdutosFornecedores> buscarPorFornecedor(Integer fornecedorId) {
        return produtosFornecedoresDAO.findByFornecedorId(fornecedorId);
    }

    // Criar associação produto-fornecedor
    public Map<String, Object> criar(Integer fornecedorId, Integer produtoId) {
        Fornecedor fornecedor = fornecedorDAO.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Produto produto = produtoDAO.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProdutosFornecedores pf = new ProdutosFornecedores();
        ProdutosFornecedoresId id = new ProdutosFornecedoresId();
        id.setIdFornecedor(fornecedorId);
        id.setIdProduto(produtoId);
        pf.setId(id);
        pf.setIdFornecedor(fornecedor);
        pf.setIdProduto(produto);

        produtosFornecedoresDAO.save(pf);

        // Retornar resposta simples
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("fornecedorId", fornecedorId);
        resultado.put("produtoId", produtoId);
        resultado.put("status", "criado");
        return resultado;
    }

    // Deletar associação específica
    public void deletar(Integer fornecedorId, Integer produtoId) {
        produtosFornecedoresDAO.deleteByFornecedorIdAndProdutoId(fornecedorId, produtoId);
    }

    // Buscar todas as associações de forma simples
    public List<Map<String, Object>> listarTodosSimples() {
        return produtosFornecedoresDAO.findAll()
                .stream()
                .map(pf -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("fornecedorId", pf.getId().getIdFornecedor());
                    resultado.put("produtoId", pf.getId().getIdProduto());
                    return resultado;
                })
                .collect(Collectors.toList());
    }

    // Versão original para compatibilidade
    public List<ProdutosFornecedores> listarTodos() {
        return produtosFornecedoresDAO.findAll();
    }
}
