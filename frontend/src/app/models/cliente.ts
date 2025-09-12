export interface Cliente {
  idCliente: number;
  nome: string;
  limiteCredito?: number;
  dataCadastro?: string; // ISO date string (YYYY-MM-DD)
  emails?: string[];
  telefones?: string[];
}

