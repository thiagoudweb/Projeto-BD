package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "Produtos_Idiomas")
public class ProdutoIdioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "idioma", length = 10, nullable = false)
    private String idioma;

    @Column(name = "nome_traduzido")
    private String nomeTraduzido;

    @Nationalized
    @Lob
    @Column(name = "descricao_traduzida")
    private String descricaoTraduzida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    @JsonIgnore
    private Produto produto;
}
