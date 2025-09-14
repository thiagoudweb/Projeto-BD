import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Produto, ItemCarrinho, PedidoCompra } from '../models/marketplace';

@Injectable({
  providedIn: 'root'
})
export class MarketplaceService {
  private apiUrl = 'http://localhost:8080/api/marketplace';

  // Carrinho de compras reativo
  private carrinhoSubject = new BehaviorSubject<ItemCarrinho[]>([]);
  public carrinho$ = this.carrinhoSubject.asObservable();

  // Contador de itens no carrinho
  private contadorItensSubject = new BehaviorSubject<number>(0);
  public contadorItens$ = this.contadorItensSubject.asObservable();

  constructor(private http: HttpClient) { }

  // Endpoints do backend
  listarProdutos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/produtos`);
  }

  buscarProduto(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/produtos/${id}`);
  }

  processarCompra(pedido: PedidoCompra): Observable<any> {
    return this.http.post(`${this.apiUrl}/comprar`, pedido);
  }

  // Gerenciamento do carrinho
  adicionarAoCarrinho(produto: Produto, quantidade: number = 1): void {
    const carrinhoAtual = this.carrinhoSubject.value;
    const itemExistente = carrinhoAtual.find(item => item.produto.idProduto === produto.idProduto);

    if (itemExistente) {
      itemExistente.quantidade += quantidade;
    } else {
      carrinhoAtual.push({ produto, quantidade });
    }

    this.carrinhoSubject.next([...carrinhoAtual]);
    this.atualizarContadorItens();
  }

  removerDoCarrinho(idProduto: number): void {
    const carrinhoAtual = this.carrinhoSubject.value;
    const novoCarrinho = carrinhoAtual.filter(item => item.produto.idProduto !== idProduto);

    this.carrinhoSubject.next(novoCarrinho);
    this.atualizarContadorItens();
  }

  atualizarQuantidade(idProduto: number, novaQuantidade: number): void {
    if (novaQuantidade <= 0) {
      this.removerDoCarrinho(idProduto);
      return;
    }

    const carrinhoAtual = this.carrinhoSubject.value;
    const item = carrinhoAtual.find(item => item.produto.idProduto === idProduto);

    if (item) {
      item.quantidade = novaQuantidade;
      this.carrinhoSubject.next([...carrinhoAtual]);
      this.atualizarContadorItens();
    }
  }

  limparCarrinho(): void {
    this.carrinhoSubject.next([]);
    this.atualizarContadorItens();
  }

  obterCarrinho(): ItemCarrinho[] {
    return this.carrinhoSubject.value;
  }

  calcularTotal(): number {
    return this.carrinhoSubject.value.reduce((total, item) => {
      return total + (item.produto.precoProduto * item.quantidade);
    }, 0);
  }

  private atualizarContadorItens(): void {
    const totalItens = this.carrinhoSubject.value.reduce((total, item) => total + item.quantidade, 0);
    this.contadorItensSubject.next(totalItens);
  }
}
