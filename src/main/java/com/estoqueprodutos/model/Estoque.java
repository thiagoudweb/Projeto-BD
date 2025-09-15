package com.estoqueprodutos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private Produto idProduto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_armazem", nullable = false)
    @JsonIgnore
    private Armazen idArmazem;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @ColumnDefault("0")
    @Column(name = "quantidade_existente", nullable = false)
    private Integer quantidadeExistente;

    // Helper methods for Produto ID
    @JsonProperty("idProduto")
    public Integer getIdProdutoId() {
        return idProduto != null ? idProduto.getIdProduto() : null;
    }

    @JsonProperty("idProduto")
    public void setIdProdutoId(Integer idProduto) {
        if (idProduto != null) {
            if (this.idProduto == null) {
                this.idProduto = new Produto();
            }
            this.idProduto.setIdProduto(idProduto);
        }
    }

    // Helper methods for Armazem ID
    @JsonProperty("idArmazem")
    public Integer getIdArmazemId() {
        return idArmazem != null ? idArmazem.getId() : null;
    }

    @JsonProperty("idArmazem")
    public void setIdArmazemId(Integer idArmazem) {
        if (idArmazem != null) {
            if (this.idArmazem == null) {
                this.idArmazem = new Armazen();
            }
            this.idArmazem.setId(idArmazem);
        }
    }
}