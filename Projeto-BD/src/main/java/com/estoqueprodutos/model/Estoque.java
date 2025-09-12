package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "Estoques")
public class Estoque {
    @Id
    @Column(name = "id_estoque", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto idProduto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_armazem", nullable = false)
    private Armazen idArmazem;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @ColumnDefault("0")
    @Column(name = "quantidade_existente", nullable = false)
    private Integer quantidadeExistente;

}