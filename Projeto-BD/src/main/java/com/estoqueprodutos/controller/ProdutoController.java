package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Produto;
import com.estoqueprodutos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Endpoint para LISTAR todos os produtos (GET /api/produtos)
    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    // Endpoint para BUSCAR um produto por ID (GET /api/produtos/1)
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para CRIAR um novo produto (POST /api/produtos)
    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }

    // Endpoint para ATUALIZAR um produto (PUT /api/produtos/1)
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto produtoDetalhes) {
        return produtoService.buscarPorId(id)
                .map(produtoExistente -> {
                    produtoExistente.setDataGarantia(produtoDetalhes.getDataGarantia());
                    produtoExistente.setStatus(produtoDetalhes.getStatus());
                    produtoExistente.setPrecoProduto(produtoDetalhes.getPrecoProduto());
                    produtoExistente.setPrecoVendaMinimo(produtoDetalhes.getPrecoVendaMinimo());
                    produtoExistente.setIdiomas(produtoDetalhes.getIdiomas());
                    Produto atualizado = produtoService.salvar(produtoExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para DELETAR um produto (DELETE /api/produtos/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        return produtoService.buscarPorId(id)
                .map(produto -> {
                    produtoService.deletar(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
