package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
    @JsonIgnore
    private Cliente cliente;

    @OneToMany(mappedBy = "idPedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PedidosProduto> produtos = new ArrayList<>();

    // Lista transitória para IDs dos produtos (não persistida no banco)
    @Transient
    private List<Integer> idsProdutos = new ArrayList<>();

    // Propriedade virtual para serialização JSON - envia apenas os IDs dos produtos
    @JsonProperty("produtos")
    public List<Integer> getIdsProdutos() {
        return idsProdutos;
    }

    @JsonProperty("produtos")
    public void setIdsProdutos(List<Integer> idsProdutos) {
        this.idsProdutos = idsProdutos != null ? idsProdutos : new ArrayList<>();
    }

    // Propriedade virtual para serialização JSON - envia apenas o ID do cliente
    @JsonProperty("idCliente")
    public Integer getIdCliente() {
        return cliente != null ? cliente.getIdCliente() : null;
    }

    // Setter virtual para deserialização JSON - aceita apenas o ID do cliente
    @JsonProperty("idCliente")
    public void setIdCliente(Integer idCliente) {
        if (idCliente != null) {
            if (this.cliente == null) {
                this.cliente = new Cliente();
            }
            this.cliente.setIdCliente(idCliente);
        }
    }

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
