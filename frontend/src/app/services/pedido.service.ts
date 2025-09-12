import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pedido } from '../models/pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private readonly baseUrl = '/api/pedidos';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.baseUrl}/${id}`);
  }

  criar(pedido: Omit<Pedido, 'idPedido'>): Observable<Pedido> {
    return this.http.post<Pedido>(this.baseUrl, pedido);
  }

  atualizar(id: number, pedido: Omit<Pedido, 'idPedido'>): Observable<Pedido> {
    return this.http.put<Pedido>(`${this.baseUrl}/${id}`, pedido);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  buscarPorCliente(idCliente: number): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.baseUrl}/cliente/${idCliente}`);
  }
}
