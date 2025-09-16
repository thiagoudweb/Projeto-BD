import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  // Base URL proxied to Spring Boot (see proxy.conf.json)
  private readonly baseUrl = '/api/clientes';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${id}`);
  }

  criar(cliente: Omit<Cliente, 'idCliente'>): Observable<Cliente> {
    return this.http.post<Cliente>(this.baseUrl, cliente);
  }

  atualizar(id: number, cliente: Omit<Cliente, 'idCliente'>): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/${id}`, cliente);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
