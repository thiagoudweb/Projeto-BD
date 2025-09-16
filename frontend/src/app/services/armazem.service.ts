import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Armazem } from '../models/armazem';

@Injectable({
  providedIn: 'root'
})
export class ArmazemService {
  // Base URL proxied to Spring Boot (see proxy.conf.json)
  private readonly baseUrl = '/api/armazens';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Armazem[]> {
    return this.http.get<Armazem[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Armazem> {
    return this.http.get<Armazem>(`${this.baseUrl}/${id}`);
  }

  criar(armazem: Omit<Armazem, 'id'>): Observable<Armazem> {
    return this.http.post<Armazem>(this.baseUrl, armazem);
  }

  atualizar(id: number, armazem: Omit<Armazem, 'id'>): Observable<Armazem> {
    return this.http.put<Armazem>(`${this.baseUrl}/${id}`, armazem);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
