package com.estoqueprodutos.service;

import com.estoqueprodutos.dao.interfaces.ICategoriaDAO;
import com.estoqueprodutos.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final ICategoriaDAO categoriaDAO;

    public CategoriaService(ICategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public Categoria salvar(Categoria categoria) {
        if (categoria.getId() == null) {
            return categoriaDAO.save(categoria);
        } else {
            return categoriaDAO.update(categoria);
        }
    }

    public List<Categoria> listarTodos() {
        return categoriaDAO.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaDAO.findById(id);
    }

    public void deleteById(Integer id) {
        categoriaDAO.deleteById(id);
    }
}