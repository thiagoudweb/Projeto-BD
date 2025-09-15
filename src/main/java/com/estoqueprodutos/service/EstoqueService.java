package com.estoqueprodutos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoqueprodutos.dao.interfaces.IEstoqueDAO;
import com.estoqueprodutos.model.Estoque;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {
    private final IEstoqueDAO estoqueDAO;

    @Autowired
    public EstoqueService(IEstoqueDAO estoqueDAO) {
        this.estoqueDAO = estoqueDAO;
    }

    @Transactional
    public Estoque salvar(Estoque estoque) {
        if (estoque.getId() == null) {
            return estoqueDAO.save(estoque);
        } else {
            return estoqueDAO.update(estoque);
        }
    }

    public List<Estoque> listarTodos() {
        return estoqueDAO.findAll();
    }

    public Optional<Estoque> buscarPorId(Integer id) {
        return estoqueDAO.findById(id);
    }

    public void deletar(Integer id) {
        estoqueDAO.deleteById(id);
    }
}
