import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MarketplaceService } from '../../services/marketplace.service';
import { Produto, ItemCarrinho } from '../../models/marketplace';

@Component({
  selector: 'app-marketplace',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './marketplace.component.html',
  styleUrls: ['./marketplace.component.css']
})
export class MarketplaceComponent implements OnInit {
  produtos: Produto[] = [];
  carrinho: ItemCarrinho[] = [];
  contadorItens = 0;
  mostrarCarrinho = false;
  idCliente: number | null = null;
  quantidades: { [key: number]: number } = {};
  carregandoCompra = false;
  mensagemSucesso = '';
  mensagemErro = '';

  constructor(private marketplaceService: MarketplaceService) {}

  ngOnInit(): void {
    this.carregarProdutos();
    this.observarCarrinho();
  }

  carregarProdutos(): void {
    this.marketplaceService.listarProdutos().subscribe({
      next: (produtos) => {
        this.produtos = produtos;
        // Inicializar quantidades com 1 para cada produto
        produtos.forEach(produto => {
          if (produto.idProduto) {
            this.quantidades[produto.idProduto] = 1;
          }
        });
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
        this.mostrarMensagemErro('Erro ao carregar produtos');
      }
    });
  }

  observarCarrinho(): void {
    this.marketplaceService.carrinho$.subscribe(carrinho => {
      this.carrinho = carrinho;
    });

    this.marketplaceService.contadorItens$.subscribe(contador => {
      this.contadorItens = contador;
    });
  }

  private normalizarStatus(status?: string): string {
    return (status || '').trim().toUpperCase();
  }

  isProdutoIndisponivel(produto: Produto): boolean {
    const s = this.normalizarStatus(produto.status);
    return ['INDISPONIVEL','INATIVO','DESCONTINUADO'].includes(s);
  }

  adicionarAoCarrinho(produto: Produto): void {
    if (this.isProdutoIndisponivel(produto)) {
      this.mostrarMensagemErro('Produto indisponível');
      return;
    }
    const quantidade = produto.idProduto ? this.quantidades[produto.idProduto] || 1 : 1;
    this.marketplaceService.adicionarAoCarrinho(produto, quantidade);
    this.mostrarMensagemSucesso(`${quantidade} item(s) adicionado(s) ao carrinho!`);
  }

  removerDoCarrinho(idProduto: number): void {
    this.marketplaceService.removerDoCarrinho(idProduto);
  }

  atualizarQuantidadeCarrinho(idProduto: number, novaQuantidade: number): void {
    this.marketplaceService.atualizarQuantidade(idProduto, novaQuantidade);
  }

  toggleCarrinho(): void {
    this.mostrarCarrinho = !this.mostrarCarrinho;
  }

  fecharCarrinho(): void {
    this.mostrarCarrinho = false;
  }

  calcularTotal(): number {
    return this.marketplaceService.calcularTotal();
  }

  finalizarCompra(): void {
    if (!this.idCliente) {
      this.mostrarMensagemErro('Por favor, informe o ID do cliente');
      return;
    }

    if (this.carrinho.length === 0) {
      this.mostrarMensagemErro('Carrinho está vazio');
      return;
    }

    this.carregandoCompra = true;

    const pedido = {
      idCliente: this.idCliente,
      modoEncomenda: 'ONLINE',
      itens: this.carrinho.map(item => ({
        idProduto: item.produto.idProduto!,
        quantidade: item.quantidade
      }))
    };

    this.marketplaceService.processarCompra(pedido).subscribe({
      next: (response) => {
        this.mostrarMensagemSucesso('Compra realizada com sucesso! Estoque atualizado automaticamente.');
        this.marketplaceService.limparCarrinho();
        this.idCliente = null;
        this.fecharCarrinho();
        this.carregandoCompra = false;
      },
      error: (error) => {
        console.error('Erro ao processar compra:', error);

        // Tratar erros específicos de estoque
        let mensagemErro = 'Erro ao processar compra. Verifique os dados e tente novamente.';

        if (error.error && typeof error.error === 'string') {
          const errorMessage = error.error.toLowerCase();

          if (errorMessage.includes('estoque insuficiente')) {
            mensagemErro = 'Estoque insuficiente para um ou mais produtos no carrinho. ' + error.error;
          } else if (errorMessage.includes('produto') && errorMessage.includes('não encontrado')) {
            mensagemErro = 'Um ou mais produtos não foram encontrados. ' + error.error;
          } else if (errorMessage.includes('cliente') && errorMessage.includes('não encontrado')) {
            mensagemErro = 'Cliente não encontrado. Verifique o ID do cliente.';
          } else {
            mensagemErro = error.error;
          }
        } else if (error.message) {
          mensagemErro = error.message;
        }

        this.mostrarMensagemErro(mensagemErro);
        this.carregandoCompra = false;
      }
    });
  }

  obterNomeProduto(produto: Produto): string {
    if (!produto.idiomas || produto.idiomas.length === 0) {
      return `Produto ${produto.idProduto}`;
    }
    return produto.idiomas[0].nomeTraduzido || '-';
  }

  obterDescricaoProduto(produto: Produto): string {
    if (!produto.idiomas || produto.idiomas.length === 0) {
      return `Produto sem descrição`;
    }
    return produto.idiomas[0].descricaoTraduzida || '-';
  }

  private mostrarMensagemSucesso(mensagem: string): void {
    this.mensagemSucesso = mensagem;
    this.mensagemErro = '';
    setTimeout(() => this.mensagemSucesso = '', 3000);
  }

  private mostrarMensagemErro(mensagem: string): void {
    this.mensagemErro = mensagem;
    this.mensagemSucesso = '';
    setTimeout(() => this.mensagemErro = '', 5000);
  }

  getProductRows(): Produto[][] {
    const rows: Produto[][] = [];
    for (let i = 0; i < this.produtos.length; i += 4) {
      rows.push(this.produtos.slice(i, i + 4));
    }
    return rows;
  }

  aumentarQuantidade(idProduto: number): void {
    if (this.quantidades[idProduto]) {
      this.quantidades[idProduto]++;
    } else {
      this.quantidades[idProduto] = 2;
    }
  }

  diminuirQuantidade(idProduto: number): void {
    if (this.quantidades[idProduto] && this.quantidades[idProduto] > 1) {
      this.quantidades[idProduto]--;
    }
  }


}
