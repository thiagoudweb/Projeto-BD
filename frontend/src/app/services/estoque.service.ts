import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Estoque } from '../models/estoque';

@Injectable({
  providedIn: 'root'
})
export class EstoqueService {
  private apiUrl = 'http://localhost:8080/api/estoques';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Estoque[]> {
    return this.http.get<Estoque[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Estoque> {
    return this.http.get<Estoque>(`${this.apiUrl}/${id}`);
  }

  criar(estoque: Estoque): Observable<Estoque> {
    return this.http.post<Estoque>(this.apiUrl, estoque);
  }

  atualizar(id: number, estoque: Estoque): Observable<Estoque> {
    return this.http.put<Estoque>(`${this.apiUrl}/${id}`, estoque);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
