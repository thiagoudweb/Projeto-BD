import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Estoque } from '../../models/estoque';
import { EstoqueService } from '../../services/estoque.service';

@Component({
  selector: 'app-estoque-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './estoque-list.component.html',
  styleUrl: './estoque-list.component.css'
})
export class EstoqueListComponent implements OnInit {
  estoques: Estoque[] = [];
  loading = false;
  error: string | null = null;

  // Form state
  showForm = false;
  editingEstoque: Estoque | null = null;
  formData: Estoque = {
    id: undefined,
    idProduto: 0,
    idArmazem: 0,
    codigo: '',
    quantidadeExistente: 0
  };

  constructor(private estoqueService: EstoqueService) {}

  ngOnInit(): void {
    this.carregarEstoques();
  }

  carregarEstoques(): void {
    this.loading = true;
    this.error = null;

    this.estoqueService.listarTodos().subscribe({
      next: (estoques) => {
        this.estoques = estoques;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar estoques';
        this.loading = false;
        console.error('Erro:', error);
      }
    });
  }

  showCreateForm(): void {
    this.editingEstoque = null;
    this.formData = {
      id: undefined,
      idProduto: 0,
      idArmazem: 0,
      codigo: '',
      quantidadeExistente: 0
    };
    this.showForm = true;
  }

  editEstoque(estoque: Estoque): void {
    this.editingEstoque = estoque;
    this.formData = { ...estoque };
    this.showForm = true;
  }

  onSubmit(): void {
    if (this.formData.idProduto <= 0 || this.formData.idArmazem <= 0) {
      this.error = 'Por favor, preencha todos os campos obrigatórios';
      return;
    }

    this.error = null;

    if (this.editingEstoque) {
      // Atualizar estoque existente
      this.estoqueService.atualizar(this.editingEstoque.id!, this.formData).subscribe({
        next: () => {
          this.carregarEstoques();
          this.showForm = false;
          this.editingEstoque = null;
        },
        error: (error) => {
          this.error = 'Erro ao atualizar estoque';
          console.error('Erro:', error);
        }
      });
    } else {
      // Criar novo estoque
      this.estoqueService.criar(this.formData).subscribe({
        next: () => {
          this.carregarEstoques();
          this.showForm = false;
        },
        error: (error) => {
          this.error = 'Erro ao criar estoque';
          console.error('Erro:', error);
        }
      });
    }
  }

  deleteEstoque(estoque: Estoque): void {
    if (confirm(`Tem certeza que deseja excluir o estoque ${estoque.codigo}?`)) {
      this.estoqueService.deletar(estoque.id!).subscribe({
        next: () => {
          this.carregarEstoques();
        },
        error: (error) => {
          this.error = 'Erro ao excluir estoque';
          console.error('Erro:', error);
        }
      });
    }
  }
}
