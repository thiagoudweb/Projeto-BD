package com.estoqueprodutos.dao.interfaces;

import com.estoqueprodutos.model.Endereco;
import java.util.List;
import java.util.Optional;

public interface IEnderecoDAO {
    Endereco save(Endereco endereco);
    Endereco update(Endereco endereco);
    void deleteById(Integer id);
    Optional<Endereco> findById(Integer id);
    List<Endereco> findAll();
}
