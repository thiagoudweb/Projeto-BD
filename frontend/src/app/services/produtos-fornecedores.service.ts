import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProdutoFornecedor, ProdutoFornecedorSimples } from '../models/produtos-fornecedores';
import { Produto } from '../models/produto';

@Injectable({
  providedIn: 'root'
})
export class ProdutosFornecedoresService {
  // Base URL proxied to Spring Boot (see proxy.conf.json)
  private readonly baseUrl = '/api/produtos-fornecedores';

  constructor(private http: HttpClient) {}

  // Buscar apenas IDs dos produtos por fornecedor
  buscarProdutoIdsPorFornecedor(fornecedorId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/${fornecedorId}/produtos-ids`);
  }

  // Buscar produtos completos por fornecedor (com dados do produto)
  buscarProdutosCompletosPorFornecedor(fornecedorId: number): Observable<ProdutoFornecedor[]> {
    return this.http.get<ProdutoFornecedor[]>(`${this.baseUrl}/${fornecedorId}/completo`);
  }

  // Criar associação produto-fornecedor
  criarAssociacao(fornecedorId: number, produtoId: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${fornecedorId}/produto/${produtoId}`, {});
  }

  // Deletar associação específica
  deletarAssociacao(fornecedorId: number, produtoId: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${fornecedorId}/produto/${produtoId}`);
  }

  // Listar todas as associações (formato simples)
  listarTodosSimples(): Observable<ProdutoFornecedorSimples[]> {
    return this.http.get<ProdutoFornecedorSimples[]>(`${this.baseUrl}/simples`);
  }
}
