package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Produto;
import com.estoqueprodutos.model.Pedido;
import com.estoqueprodutos.model.dto.PedidoCompraDTO;
import com.estoqueprodutos.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
@CrossOrigin(origins = "http://localhost:4200")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    /**
     * GET /api/marketplace/produtos
     * Lista todos os produtos disponíveis para compra no marketplace
     */
    @GetMapping("/produtos")
    public List<Produto> listarProdutos() {
        return marketplaceService.listarProdutosDisponiveis();
    }

    /**
     * GET /api/marketplace/produtos/{id}
     * Busca um produto específico por ID
     */
    @GetMapping("/produtos/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Integer id) {
        return marketplaceService.buscarProduto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/marketplace/comprar
     * Processa uma compra no marketplace
     *
     * Exemplo de JSON para o body:
     * {
     *   "idCliente": 1,
     *   "modoEncomenda": "ONLINE",
     *   "itens": [
     *     {
     *       "idProduto": 1,
     *       "quantidade": 2
     *     },
     *     {
     *       "idProduto": 3,
     *       "quantidade": 1
     *     }
     *   ]
     * }
     */
    @PostMapping("/comprar")
    public ResponseEntity<Pedido> processarCompra(@RequestBody PedidoCompraDTO pedidoCompraDTO) {
        try {
            Pedido pedidoCriado = marketplaceService.processarCompra(pedidoCompraDTO);
            return ResponseEntity.ok(pedidoCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


