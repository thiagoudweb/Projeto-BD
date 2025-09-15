package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Estoque;
import com.estoqueprodutos.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
@CrossOrigin(origins = "http://localhost:4200")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    // Endpoint para LISTAR todos os estoques (GET /api/estoques)
    @GetMapping
    public List<Estoque> listarTodos() {
        return estoqueService.listarTodos();
    }

    // Endpoint para BUSCAR um estoque por ID (GET /api/estoques/1)
    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Integer id) {
        return estoqueService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para CRIAR um novo estoque (POST /api/estoques)
    @PostMapping
    public Estoque criar(@RequestBody Estoque estoque) {
        return estoqueService.salvar(estoque);
    }

    // Endpoint para ATUALIZAR um estoque (PUT /api/estoques/1)
    @PutMapping("/{id}")
    public ResponseEntity<Estoque> atualizar(@PathVariable Integer id, @RequestBody Estoque estoqueDetalhes) {
        return estoqueService.buscarPorId(id)
                .map(estoqueExistente -> {
                    estoqueExistente.setIdProdutoId(estoqueDetalhes.getIdProdutoId());
                    estoqueExistente.setCodigo(estoqueDetalhes.getCodigo());
                    estoqueExistente.setQuantidadeExistente(estoqueDetalhes.getQuantidadeExistente());
                    Estoque atualizado = estoqueService.salvar(estoqueExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para DELETAR um estoque (DELETE /api/estoques/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        return estoqueService.buscarPorId(id)
                .map(estoque -> {
                    estoqueService.deletar(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
