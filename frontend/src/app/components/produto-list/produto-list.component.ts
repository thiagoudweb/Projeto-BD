import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Produto } from '../../models/produto';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './produto-list.component.html',
  styleUrl: './produto-list.component.css'
})
export class ProdutoListComponent implements OnInit {
  produtos: Produto[] = [];
  loading = false;
  error: string | null = null;

  // Form state
  showForm = false;
  editingProduto: Produto | null = null;
  formData: Produto = {
    idProduto: undefined,
    dataGarantia: '',
    status: 'Ativo',
    precoProduto: 0,
    precoVendaMinimo: 0,
    idiomas: []
  };

  constructor(private produtoService: ProdutoService) {}

  ngOnInit(): void {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.loading = true;
    this.error = null;

    this.produtoService.listarTodos().subscribe({
      next: (produtos) => {
        this.produtos = produtos;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar produtos';
        this.loading = false;
        console.error('Erro:', error);
      }
    });
  }

  showCreateForm(): void {
    this.editingProduto = null;
    this.formData = {
      idProduto: undefined,
      dataGarantia: '',
      status: 'Ativo',
      precoProduto: 0,
      precoVendaMinimo: 0,
      idiomas: []
    };
    this.showForm = true;
  }

  editProduto(produto: Produto): void {
    this.editingProduto = produto;
    this.formData = { ...produto };
    this.showForm = true;
  }

  deleteProduto(produto: Produto): void {
    if (!produto.idProduto) return;
    if (confirm('Tem certeza que deseja deletar este produto?')) {
      this.produtoService.deletar(produto.idProduto).subscribe({
        next: () => {
          this.carregarProdutos();
        },
        error: (error) => {
          this.error = 'Erro ao deletar produto';
          console.error('Erro:', error);
        }
      });
    }
  }

  onSubmit(): void {
    this.error = null;

    const payload: Produto = {
      idProduto: this.formData.idProduto,
      dataGarantia: this.formData.dataGarantia || '',
      status: this.formData.status || 'Ativo',
      precoProduto: Number(this.formData.precoProduto) || 0,
      precoVendaMinimo: Number(this.formData.precoVendaMinimo) || 0,
      idiomas: this.formData.idiomas || []
    };

    if (this.editingProduto && this.editingProduto.idProduto) {
      this.produtoService.atualizar(this.editingProduto.idProduto, payload).subscribe({
        next: () => {
          this.showForm = false;
          this.carregarProdutos();
        },
        error: (err) => {
          console.error(err);
          this.error = 'Erro ao atualizar produto';
        }
      });
    } else {
      this.produtoService.criar(payload).subscribe({
        next: () => {
          this.showForm = false;
          this.carregarProdutos();
        },
        error: (err) => {
          console.error(err);
          this.error = 'Erro ao criar produto';
        }
      });
    }
  }

  // Helper methods
  getPrimeiroNomeTraduzido(produto: Produto): string {
    if (!produto.idiomas || produto.idiomas.length === 0) {
      return '-';
    }
    return produto.idiomas[0].nomeTraduzido || '-';
  }

  getPrimeiraDescricaoTraduzida(produto: Produto): string {
    if (!produto.idiomas || produto.idiomas.length === 0) {
      return '-';
    }
    return produto.idiomas[0].descricaoTraduzida || '-';
  }

  adicionarProduto(): void {
    this.showCreateForm();
  }

  editarProduto(produto: Produto): void {
    this.editProduto(produto);
  }

  deletarProduto(produto: Produto): void {
    this.deleteProduto(produto);
  }

  // Helpers
  formatarMoeda(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor ?? 0);
  }

  formatarData(data: string | undefined): string {
    if (!data) return '-';
    try {
      return new Date(data).toLocaleDateString('pt-BR');
    } catch {
      return '-';
    }
  }
}
