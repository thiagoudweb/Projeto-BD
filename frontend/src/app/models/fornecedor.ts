export interface Fornecedor {
  id?: number;
  nome: string;
  tipo: string; // 'F' for Pessoa Física or 'J' for Pessoa Jurídica
  cpf?: string;
  cnpj?: string;
  pais: string;
}

