package com.estoqueprodutos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProdutosCategoriaId implements java.io.Serializable {
    private static final long serialVersionUID = -3679811454095066186L;
    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;

    @Column(name = "id_categoria", nullable = false)
    private Integer idCategoria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProdutosCategoriaId entity = (ProdutosCategoriaId) o;
        return Objects.equals(this.idProduto, entity.idProduto) &&
                Objects.equals(this.idCategoria, entity.idCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, idCategoria);
    }

}