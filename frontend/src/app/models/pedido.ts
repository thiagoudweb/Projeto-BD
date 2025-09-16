export interface Pedido {
  idPedido: number;
  idCliente: number;
  dataPedido?: string; // ISO date string (YYYY-MM-DD) - LocalDate from backend
  prazoEntrega?: string; // ISO date string (YYYY-MM-DD) - LocalDate from backend
  precoFinal: number;
  modoEncomenda: string;
  status: string;
  produtos?: number[]; // Array de IDs dos produtos relacionados ao pedido
}

export interface PedidoProduto {
  idProduto: number;
  quantidade: number;
}
