package com.estoqueprodutos.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoCompraDTO {
    private Integer idCliente;
    private List<ItemCompraDTO> itens;
    private String modoEncomenda;

    @Getter
    @Setter
    public static class ItemCompraDTO {
        private Integer idProduto;
        private Integer quantidade;
    }
}
