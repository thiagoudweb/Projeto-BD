import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FornecedorService } from '../../services/fornecedor.service';
import { ProdutosFornecedoresService } from '../../services/produtos-fornecedores.service';
import { ProdutoService } from '../../services/produto.service';
import { Fornecedor } from '../../models/fornecedor';
import { ProdutoFornecedor } from '../../models/produtos-fornecedores';
import { Produto } from '../../models/produto';

@Component({
  selector: 'app-fornecedor-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './fornecedor-details.component.html',
  styleUrl: './fornecedor-details.component.css'
})
export class FornecedorDetailsComponent implements OnInit {
  fornecedorId: string = '';
  fornecedor: Fornecedor | null = null;
  produtosFornecedor: ProdutoFornecedor[] = [];
  todosOsProdutos: Produto[] = [];

  // Estados da interface
  carregandoFornecedor = false;
  carregandoProdutos = false;
  erroFornecedor = '';
  erroProdutos = '';

  // Modal de adicionar produto
  mostrarModal = false;
  carregandoModal = false;
  produtoSelecionado: number = 0;

  constructor(
    private fornecedorService: FornecedorService,
    private produtosFornecedoresService: ProdutosFornecedoresService,
    private produtoService: ProdutoService
  ) {}

  ngOnInit(): void {
    this.carregarTodosOsProdutos();
  }

  buscarFornecedor(): void {
    const id = parseInt(this.fornecedorId.trim());

    if (!this.fornecedorId.trim() || isNaN(id) || id <= 0) {
      this.erroFornecedor = 'Por favor, digite um ID válido';
      return;
    }

    this.carregandoFornecedor = true;
    this.erroFornecedor = '';
    this.fornecedor = null;
    this.produtosFornecedor = [];

    // Buscar dados do fornecedor
    this.fornecedorService.buscarPorId(id).subscribe({
      next: (fornecedor) => {
        this.fornecedor = fornecedor;
        this.carregandoFornecedor = false;
        this.buscarProdutosFornecedor();
      },
      error: (error) => {
        this.erroFornecedor = 'Fornecedor não encontrado';
        this.carregandoFornecedor = false;
        console.error('Erro ao buscar fornecedor:', error);
      }
    });
  }

  buscarProdutosFornecedor(): void {
    const id = parseInt(this.fornecedorId.trim());

    this.carregandoProdutos = true;
    this.erroProdutos = '';

    this.produtosFornecedoresService.buscarProdutosCompletosPorFornecedor(id).subscribe({
      next: (produtos) => {
        this.produtosFornecedor = produtos;
        this.carregandoProdutos = false;
      },
      error: (error) => {
        this.erroProdutos = 'Erro ao carregar produtos do fornecedor';
        this.carregandoProdutos = false;
        console.error('Erro ao buscar produtos do fornecedor:', error);
      }
    });
  }

  carregarTodosOsProdutos(): void {
    this.produtoService.listarTodos().subscribe({
      next: (produtos) => {
        this.todosOsProdutos = produtos;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }

  abrirModalAdicionarProduto(): void {
    this.mostrarModal = true;
    this.produtoSelecionado = 0;
  }

  fecharModal(): void {
    this.mostrarModal = false;
    this.produtoSelecionado = 0;
  }

  adicionarProduto(): void {
    if (this.produtoSelecionado <= 0) {
      alert('Por favor, selecione um produto');
      return;
    }

    const fornecedorIdNum = parseInt(this.fornecedorId.trim());
    this.carregandoModal = true;

    this.produtosFornecedoresService.criarAssociacao(fornecedorIdNum, this.produtoSelecionado).subscribe({
      next: (response) => {
        this.carregandoModal = false;
        this.fecharModal();
        this.buscarProdutosFornecedor(); // Recarregar lista
        alert('Produto adicionado com sucesso!');
      },
      error: (error) => {
        this.carregandoModal = false;
        alert('Erro ao adicionar produto. Pode ser que já esteja associado ao fornecedor.');
        console.error('Erro ao adicionar produto:', error);
      }
    });
  }

  removerProduto(produtoId: number): void {
    if (confirm('Tem certeza que deseja remover este produto do fornecedor?')) {
      const fornecedorIdNum = parseInt(this.fornecedorId.trim());

      this.produtosFornecedoresService.deletarAssociacao(fornecedorIdNum, produtoId).subscribe({
        next: (response) => {
          this.buscarProdutosFornecedor(); // Recarregar lista
          alert('Produto removido com sucesso!');
        },
        error: (error) => {
          alert('Erro ao remover produto');
          console.error('Erro ao remover produto:', error);
        }
      });
    }
  }

  limparBusca(): void {
    this.fornecedorId = '';
    this.fornecedor = null;
    this.produtosFornecedor = [];
    this.erroFornecedor = '';
    this.erroProdutos = '';
  }

  // Função auxiliar para obter o nome do produto no select
  obterNomeProduto(produto: Produto): string {
    if (produto.idiomas && produto.idiomas.length > 0) {
      return produto.idiomas[0].nomeTraduzido;
    }
    return `Produto ${produto.idProduto}`;
  }

  // Função auxiliar para verificar se um produto já está associado
  produtoJaAssociado(produtoId: number): boolean {
    return this.produtosFornecedor.some(pf => pf.produtoId === produtoId);
  }
}
