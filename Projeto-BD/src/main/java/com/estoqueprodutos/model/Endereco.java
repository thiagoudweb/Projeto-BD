package com.estoqueprodutos.model;

import com.estoqueprodutos.model.Cliente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco", nullable = false)
    private Integer id;

    @Column(name = "pais", length = 100)
    private String pais;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entregadora")
    private Entregadora idEntregadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fornecedor")
    private Fornecedore idFornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_armazem")
    private Armazen idArmazem;

}