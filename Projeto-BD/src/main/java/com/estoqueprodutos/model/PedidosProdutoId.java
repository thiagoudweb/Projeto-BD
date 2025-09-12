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
public class PedidosProdutoId implements java.io.Serializable {
    private static final long serialVersionUID = 6752056098719867375L;
    @Column(name = "id_pedido", nullable = false)
    private Integer idPedido;

    @Column(name = "id_produto", nullable = false)
    private Integer idProduto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PedidosProdutoId entity = (PedidosProdutoId) o;
        return Objects.equals(this.idProduto, entity.idProduto) &&
                Objects.equals(this.idPedido, entity.idPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, idPedido);
    }

}