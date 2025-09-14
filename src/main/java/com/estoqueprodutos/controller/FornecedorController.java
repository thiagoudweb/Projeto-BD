package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Fornecedor;
import com.estoqueprodutos.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta classe é um controller REST
@RequestMapping("/api/fornecedores") // URL base para todos os endpoints deste controller
@CrossOrigin(origins = "http://localhost:4200") // Permite requisições do Angular (rodando na porta 4200)
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    // Endpoint para LISTAR todos os fornecedores (GET /api/fornecedores)
    @GetMapping
    public List<Fornecedor> listarTodos() {
        return fornecedorService.listarTodos();
    }

    // Endpoint para BUSCAR um fornecedor por ID (GET /api/fornecedores/1)
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Integer id) {
        return fornecedorService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o fornecedor
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // Endpoint para CRIAR um novo fornecedor (POST /api/fornecedores)
    @PostMapping
    public Fornecedor criar(@RequestBody Fornecedor fornecedor) {
        return fornecedorService.salvar(fornecedor);
    }

    // Endpoint para ATUALIZAR um fornecedor (PUT /api/fornecedores/1)
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Integer id, @RequestBody Fornecedor fornecedorDetalhes) {
        return fornecedorService.buscarPorId(id)
                .map(fornecedorExistente -> {
                    fornecedorExistente.setNome(fornecedorDetalhes.getNome());
                    fornecedorExistente.setTipo(fornecedorDetalhes.getTipo());
                    fornecedorExistente.setCpf(fornecedorDetalhes.getCpf());
                    fornecedorExistente.setCnpj(fornecedorDetalhes.getCnpj());
                    fornecedorExistente.setPais(fornecedorDetalhes.getPais());
                    Fornecedor atualizado = fornecedorService.salvar(fornecedorExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para EXCLUIR um fornecedor (DELETE /api/fornecedores/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        return fornecedorService.buscarPorId(id)
                .map(fornecedor -> {
                    fornecedorService.deletar(id);
                    return ResponseEntity.ok().<Void>build(); // Retorna 200 OK sem conteúdo
                })
                .orElse(ResponseEntity.notFound().build()); // Se não encontrar, retorna 404
    }
}
