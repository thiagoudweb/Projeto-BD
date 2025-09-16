package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Pedidos_Produtos")
public class PedidosProduto {
    @EmbeddedId
    private PedidosProdutoId id;

    @MapsId("idPedido")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnore
    private Pedido idPedido;

    @MapsId("idProduto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    @JsonIgnore
    private Produto idProduto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_total_produtos", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoTotalProdutos;

    // Métodos convenientes para serialização JSON
    @JsonProperty("idProduto")
    public Integer getIdProdutoValue() {
        return idProduto != null ? idProduto.getIdProduto() : null;
    }

    @JsonProperty("produto")
    public Produto getProduto() {
        return idProduto;
    }

    // Método para facilitar a criação da entidade
    public void setProdutoById(Integer produtoId) {
        if (produtoId != null) {
            if (this.idProduto == null) {
                this.idProduto = new Produto();
            }
            this.idProduto.setIdProduto(produtoId);

            // Inicializar ou atualizar o ID composto
            if (this.id == null) {
                this.id = new PedidosProdutoId();
            }
            this.id.setIdProduto(produtoId);
        }
    }

    public void setPedidoById(Integer pedidoId) {
        if (pedidoId != null) {
            if (this.idPedido == null) {
                this.idPedido = new Pedido();
            }
            this.idPedido.setIdPedido(pedidoId);

            // Inicializar ou atualizar o ID composto
            if (this.id == null) {
                this.id = new PedidosProdutoId();
            }
            this.id.setIdPedido(pedidoId);
        }
    }
}