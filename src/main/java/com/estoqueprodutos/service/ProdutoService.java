package com.estoqueprodutos.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoqueprodutos.dao.interfaces.IProdutoDAO;
import com.estoqueprodutos.model.Produto;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final IProdutoDAO produtoDAO;

    @Autowired
    public ProdutoService(IProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    @Transactional
    public Produto salvar(Produto produto) {
        if (produto.getIdProduto() == null) {
            return produtoDAO.save(produto);
        } else {
            return produtoDAO.update(produto);
        }
    }

    public List<Produto> listarTodos() {
        return produtoDAO.findAll();
    }

    public Optional<Produto> buscarPorId(Integer id) {
        return produtoDAO.findById(id);
    }

    public void deletar(Integer id) {
        produtoDAO.deleteById(id);
    }
}
