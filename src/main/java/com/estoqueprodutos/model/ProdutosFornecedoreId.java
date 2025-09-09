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
public class ProdutosFornecedoreId implements java.io.Serializable {
    private static final long serialVersionUID = -166046706769853185L;
    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;

    @Column(name = "id_fornecedor", nullable = false)
    private Integer idFornecedor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProdutosFornecedoreId entity = (ProdutosFornecedoreId) o;
        return Objects.equals(this.idProduto, entity.idProduto) &&
                Objects.equals(this.idFornecedor, entity.idFornecedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, idFornecedor);
    }

}