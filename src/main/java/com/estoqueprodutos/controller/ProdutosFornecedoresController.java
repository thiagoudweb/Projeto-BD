package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.ProdutosFornecedores;
import com.estoqueprodutos.service.ProdutosFornecedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos-fornecedores")
@CrossOrigin(origins = "*")
public class ProdutosFornecedoresController {

    @Autowired
    private ProdutosFornecedoresService service;

    // Buscar apenas IDs dos produtos por fornecedor (formato simples)
    @GetMapping("/{fornecedorId}/produtos-ids")
    public ResponseEntity<List<Integer>> buscarProdutoIds(@PathVariable Integer fornecedorId) {
        try {
            List<Integer> produtoIds = service.buscarProdutoIdsPorFornecedor(fornecedorId);
            return ResponseEntity.ok(produtoIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar produtos completos por fornecedor (com dados do produto)
    @GetMapping("/{fornecedorId}/completo")
    public ResponseEntity<List<Map<String, Object>>> buscarProdutosCompletos(@PathVariable Integer fornecedorId) {
        try {
            List<Map<String, Object>> produtos = service.buscarProdutosCompletosPorFornecedor(fornecedorId);
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint original (mantido para compatibilidade)
    @GetMapping("/{fornecedorId}")
    public ResponseEntity<List<ProdutosFornecedores>> buscarPorFornecedor(@PathVariable Integer fornecedorId) {
        try {
            List<ProdutosFornecedores> produtos = service.buscarPorFornecedor(fornecedorId);
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Criar associação produto-fornecedor (retorna formato simples)
    @PostMapping("/{fornecedorId}/produto/{produtoId}")
    public ResponseEntity<Map<String, Object>> criarAssociacao(@PathVariable Integer fornecedorId,
                                                               @PathVariable Integer produtoId) {
        try {
            Map<String, Object> resultado = service.criar(fornecedorId, produtoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Deletar associação específica
    @DeleteMapping("/{fornecedorId}/produto/{produtoId}")
    public ResponseEntity<Map<String, String>> deletarAssociacao(@PathVariable Integer fornecedorId,
                                                  @PathVariable Integer produtoId) {
        try {
            service.deletar(fornecedorId, produtoId);
            return ResponseEntity.ok(Map.of("status", "deletado", "fornecedorId", fornecedorId.toString(), "produtoId", produtoId.toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Listar todas as associações (formato simples)
    @GetMapping("/simples")
    public ResponseEntity<List<Map<String, Object>>> listarTodosSimples() {
        try {
            List<Map<String, Object>> todos = service.listarTodosSimples();
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint original (mantido para compatibilidade)
    @GetMapping
    public ResponseEntity<List<ProdutosFornecedores>> listarTodos() {
        try {
            List<ProdutosFornecedores> todos = service.listarTodos();
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
