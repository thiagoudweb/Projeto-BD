package com.estoqueprodutos.dao.interfaces;
import java.util.List;
import java.util.Optional;
import com.estoqueprodutos.model.Produto;

public interface IProdutoDAO {
    Produto save(Produto produto);
    Produto update(Produto produto);
    Optional<Produto> findById(Integer id);
    List<Produto> findAll();
    void deleteById(Integer id);

}
