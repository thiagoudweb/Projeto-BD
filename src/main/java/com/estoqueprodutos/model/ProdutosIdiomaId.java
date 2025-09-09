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
public class ProdutosIdiomaId implements java.io.Serializable {
    private static final long serialVersionUID = 5282666740839243476L;
    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;

    @Column(name = "idioma", nullable = false, length = 10)
    private String idioma;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProdutosIdiomaId entity = (ProdutosIdiomaId) o;
        return Objects.equals(this.idProduto, entity.idProduto) &&
                Objects.equals(this.idioma, entity.idioma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, idioma);
    }

}