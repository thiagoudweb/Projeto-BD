export interface ItemCarrinho {
  produto: Produto;
  quantidade: number;
}

export interface PedidoCompra {
  idCliente: number;
  modoEncomenda?: string;
  itens: ItemCompraDTO[];
}

export interface ItemCompraDTO {
  idProduto: number;
  quantidade: number;
}

export interface Produto {
  idProduto?: number;
  dataGarantia?: string;
  status?: string;
  precoProduto: number;
  precoVendaMinimo: number;
  idiomas: ProdutoIdioma[];
}

export interface ProdutoIdioma {
  id?: number;
  idioma: string;
  nomeTraduzido: string;
  descricaoTraduzida: string;
}
