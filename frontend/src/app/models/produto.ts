export interface ProdutoIdioma {
  id?: number;
  idioma: string;
  nomeTraduzido: string;
  descricaoTraduzida: string;
}

export interface Produto {
  idProduto?: number;
  dataGarantia?: string;
  status?: string;
  precoProduto: number;
  precoVendaMinimo: number;
  idiomas: ProdutoIdioma[];
}
