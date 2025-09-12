package com.estoqueprodutos.controller;
import com.estoqueprodutos.model.Cliente;
import com.estoqueprodutos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta classe é um controller REST
@RequestMapping("/api/clientes") // URL base para todos os endpoints deste controller
@CrossOrigin(origins = "http://localhost:4200") // Permite requisições do Angular (rodando na porta 4200)
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Endpoint para LISTAR todos os clientes (GET /api/clientes)
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    // Endpoint para BUSCAR um cliente por ID (GET /api/clientes/1)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o cliente
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // Endpoint para CRIAR um novo cliente (POST /api/clientes)
    @PostMapping
    public Cliente criar(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    // Endpoint para ATUALIZAR um cliente (PUT /api/clientes/1)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Integer id, @RequestBody Cliente clienteDetalhes) {
        return clienteService.buscarPorId(id)
                .map(clienteExistente -> {
                    clienteExistente.setNome(clienteDetalhes.getNome());
                    clienteExistente.setEmails(clienteDetalhes.getEmails());
                    clienteExistente.setLimiteCredito(clienteDetalhes.getLimiteCredito());
                    clienteExistente.setTelefones(clienteDetalhes.getTelefones());
                    Cliente atualizado = clienteService.salvar(clienteExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para DELETAR um cliente (DELETE /api/clientes/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!clienteService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}