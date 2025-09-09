package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Pedido;
import com.estoqueprodutos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Endpoint para LISTAR todos os pedidos
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    // Endpoint para BUSCAR um pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para CRIAR um novo pedido
    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        return pedidoService.salvar(pedido);
    }

    // Endpoint para DELETAR um pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (pedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar todos os pedidos de um cliente específico
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> buscarPedidosPorCliente(@PathVariable Integer clienteId) {
        return pedidoService.buscarPorClienteId(clienteId);
    }
}
