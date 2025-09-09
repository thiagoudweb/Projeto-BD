package com.estoqueprodutos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Entregadoras_Telefones")
public class EntregadorasTelefone {
    @EmbeddedId
    private EntregadorasTelefoneId id;

    @MapsId("idEntregadora")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_entregadora", nullable = false)
    private Entregadora idEntregadora;

}