package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Produtos_Idiomas")
public class ProdutosIdioma {
    @EmbeddedId
    private ProdutosIdiomaId id;

    @MapsId("idProduto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto idProduto;

    @Column(name = "nome_traduzido", nullable = false)
    private String nomeTraduzido;

    @Nationalized
    @Lob
    @Column(name = "descricao_traduzida")
    private String descricaoTraduzida;

}