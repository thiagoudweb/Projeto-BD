package com.estoqueprodutos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Produtos")
public class Produto {
    @Id
    @Column(name = "id_produto", nullable = false)
    private Integer id;

    @Column(name = "data_garantia")
    private LocalDate dataGarantia;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "preco_produto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoProduto;

    @Column(name = "preco_venda_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVendaMinimo;

}