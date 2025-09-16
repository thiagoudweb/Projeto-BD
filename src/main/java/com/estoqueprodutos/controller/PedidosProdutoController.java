package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.PedidosProduto;
import com.estoqueprodutos.model.PedidosProdutoId;
import com.estoqueprodutos.service.PedidosProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos-produtos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidosProdutoController {

    @Autowired
    private PedidosProdutoService pedidosProdutoService;

    // Endpoint para LISTAR todos os pedidos-produtos
    @GetMapping
    public List<PedidosProduto> listarTodos() {
        return pedidosProdutoService.listarTodos();
    }

    // Endpoint para BUSCAR produtos de um pedido específico
    @GetMapping("/pedido/{pedidoId}")
    public List<PedidosProduto> buscarPorPedidoId(@PathVariable Integer pedidoId) {
        return pedidosProdutoService.buscarPorPedidoId(pedidoId);
    }

    // Endpoint para BUSCAR um pedido-produto específico por IDs
    @GetMapping("/{pedidoId}/{produtoId}")
    public ResponseEntity<PedidosProduto> buscarPorId(@PathVariable Integer pedidoId,
                                                      @PathVariable Integer produtoId) {
        PedidosProdutoId id = new PedidosProdutoId();
        id.setIdPedido(pedidoId);
        id.setIdProduto(produtoId);

        return pedidosProdutoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para ADICIONAR um produto a um pedido
    @PostMapping("/adicionar")
    public ResponseEntity<PedidosProduto> adicionarProdutoAoPedido(
            @RequestParam Integer pedidoId,
            @RequestParam Integer produtoId,
            @RequestParam Integer quantidade) {
        try {
            PedidosProduto pedidosProduto = pedidosProdutoService.adicionarProdutoAoPedido(pedidoId, produtoId, quantidade);
            return ResponseEntity.ok(pedidosProduto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para ATUALIZAR um pedido-produto existente
    @PutMapping("/{pedidoId}/{produtoId}")
    public ResponseEntity<PedidosProduto> atualizar(@PathVariable Integer pedidoId,
                                                    @PathVariable Integer produtoId,
                                                    @RequestBody PedidosProduto pedidosProduto) {
        PedidosProdutoId id = new PedidosProdutoId();
        id.setIdPedido(pedidoId);
        id.setIdProduto(produtoId);

        if (pedidosProdutoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidosProduto.setId(id);
        return ResponseEntity.ok(pedidosProdutoService.salvar(pedidosProduto));
    }

    // Endpoint para REMOVER um produto de um pedido
    @DeleteMapping("/{pedidoId}/{produtoId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer pedidoId,
                                       @PathVariable Integer produtoId) {
        PedidosProdutoId id = new PedidosProdutoId();
        id.setIdPedido(pedidoId);
        id.setIdProduto(produtoId);

        if (pedidosProdutoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidosProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
