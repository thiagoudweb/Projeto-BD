package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Nationalized
    @Lob
    @Column(name = "descricao")
    private String descricao;

}