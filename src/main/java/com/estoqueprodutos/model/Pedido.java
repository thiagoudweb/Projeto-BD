package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido", nullable = false)
    private Integer idPedido;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Column(name = "prazo_entrega")
    private LocalDate prazoEntrega;

    @Column(name = "preco_final", precision = 10, scale = 2)
    private BigDecimal precoFinal;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "modo_encomenda", length = 50)
    private String modoEncomenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Relacionamentos com outras entidades (placeholders)
    // Descomente quando as entidades Entregadora e Endereco forem criadas
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entregadora")
    private Entregadora entregadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco_entrega")
    private Endereco enderecoEntrega;
    */
}
