package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;

    @Column(name = "data_garantia")
    private LocalDate dataGarantia;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "preco_produto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoProduto;

    @Column(name = "preco_venda_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVendaMinimo;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProdutoIdioma> idiomas = new ArrayList<>();

}
