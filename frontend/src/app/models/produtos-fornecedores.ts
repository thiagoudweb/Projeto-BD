export interface ProdutoFornecedor {
  fornecedorId: number;
  produtoId: number;
  nomeProduto?: string;
  precoProduto?: number;
  status?: string;
  dataGarantia?: string;
  descricaoProduto?: string;
  idioma?: string;
}

export interface ProdutoFornecedorSimples {
  fornecedorId: number;
  produtoId: number;
}
