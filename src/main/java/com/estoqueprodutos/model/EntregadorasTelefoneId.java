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
public class EntregadorasTelefoneId implements java.io.Serializable {
    private static final long serialVersionUID = 2884282713139643995L;
    @Column(name = "id_entregadora", nullable = false)
    private Integer idEntregadora;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EntregadorasTelefoneId entity = (EntregadorasTelefoneId) o;
        return Objects.equals(this.telefone, entity.telefone) &&
                Objects.equals(this.idEntregadora, entity.idEntregadora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telefone, idEntregadora);
    }

}