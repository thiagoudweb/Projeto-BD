package com.estoqueprodutos.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity // Marca esta classe como uma entidade JPA (mapeada para uma tabela)
@Table(name = "Clientes") // Especifica o nome da tabela no banco
@Getter // Lombok: gera os getters
@Setter // Lombok: gera os setters
public class Cliente {

    @Id // Marca este campo como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática do ID
    @Column(name = "id_cliente") // Mapeia para a coluna "id_cliente"
    private Integer idCliente;

    @Column(name = "nome", nullable = false, length = 255) // nullable=false -> NOT NULL
    private String nome;

    @Column(name = "limite_credito", precision = 10, scale = 2)
    private BigDecimal limiteCredito;


    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "Clientes_Emails", joinColumns = @JoinColumn(name = "id_cliente"))
    @Column(name = "email")
    private List<String> emails = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "Clientes_Telefones", joinColumns = @JoinColumn(name = "id_cliente"))
    @Column(name = "telefone")
    private List<String> telefones = new ArrayList<>();

    // Você pode adicionar os relacionamentos aqui depois, por exemplo:
    // @OneToMany(mappedBy = "cliente")
    // private List<Pedido> pedidos;
}
