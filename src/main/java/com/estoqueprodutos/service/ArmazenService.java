package com.estoqueprodutos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.estoqueprodutos.dao.interfaces.IArmazenDAO;
import com.estoqueprodutos.model.Armazen;

@Service
public class ArmazenService {
    private final IArmazenDAO armazenDAO;

    public ArmazenService(IArmazenDAO armazenDAO) {
        this.armazenDAO = armazenDAO;
    }

    public Armazen salvar(Armazen armazen) {
        if(armazen.getId() == null) {
            return armazenDAO.save(armazen);
        } else {
            return armazenDAO.update(armazen);
        }
    }

    public List<Armazen> listarTodos() {
        return armazenDAO.findAll();
    }

    public Optional<Armazen> buscarPorId(Integer id){
        return armazenDAO.findById(id);
    }

    public void deleteById(Integer id){
        armazenDAO.deleteById(id);
    }
}
