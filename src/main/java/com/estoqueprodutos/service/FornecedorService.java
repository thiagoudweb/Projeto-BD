package com.estoqueprodutos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoqueprodutos.dao.interfaces.IFornecedorDAO;
import com.estoqueprodutos.model.Fornecedor;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FornecedorService {
    private final IFornecedorDAO fornecedorDAO;

    @Autowired
    public FornecedorService(IFornecedorDAO fornecedorDAO) {
        this.fornecedorDAO = fornecedorDAO;
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        if (fornecedor.getId() == null) {
            return fornecedorDAO.save(fornecedor);
        } else {
            return fornecedorDAO.update(fornecedor);
        }
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorDAO.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Integer id) {
        return fornecedorDAO.findById(id);
    }

    @Transactional
    public void deletar(Integer id) {
        fornecedorDAO.deleteById(id);
    }

    @Transactional
    public Fornecedor atualizar(Fornecedor fornecedor) {
        return fornecedorDAO.update(fornecedor);
    }
}
