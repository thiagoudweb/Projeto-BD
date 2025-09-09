package com.estoqueprodutos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Entregadoras")
public class Entregadora {
    @Id
    @Column(name = "id_entregadora", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    @Column(name = "email")
    private String email;

}