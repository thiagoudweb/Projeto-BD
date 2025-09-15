package com.estoqueprodutos.controller;

import com.estoqueprodutos.model.Armazen;
import com.estoqueprodutos.service.ArmazenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armazens")
@CrossOrigin(origins = "http://localhost:4200")
public class ArmazenController {

    @Autowired
    private ArmazenService armazenService;

    @GetMapping
    public List<Armazen> listarTodos() {
        return armazenService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Armazen> buscarPorId(@PathVariable Integer id) {
        return armazenService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Armazen criar(@RequestBody Armazen armazen) {
        return armazenService.salvar(armazen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Armazen> atualizar(@PathVariable Integer id, @RequestBody Armazen armazenDetalhes) {
        return armazenService.buscarPorId(id)
                .map(armazenExistente -> {
                    armazenExistente.setNome(armazenDetalhes.getNome());
                    Armazen atualizado = armazenService.salvar(armazenExistente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        return armazenService.buscarPorId(id)
                .map(armazen -> {
                    armazenService.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}