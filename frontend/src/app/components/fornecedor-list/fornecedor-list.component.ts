import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Fornecedor } from '../../models/fornecedor';
import { FornecedorService } from '../../services/fornecedor.service';

@Component({
  selector: 'app-fornecedor-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './fornecedor-list.component.html',
  styleUrl: './fornecedor-list.component.css'
})
export class FornecedorListComponent implements OnInit {
  fornecedores: Fornecedor[] = [];
  loading = false;
  error: string | null = null;
  showForm = false;
  editingFornecedor: Fornecedor | null = null;

  // Form fields
  formData = {
    nome: '',
    tipo: 'F', // Default to Pessoa Física
    cpf: '',
    cnpj: '',
    pais: ''
  };

  constructor(private fornecedorService: FornecedorService) {}

  ngOnInit(): void {
    this.fetchFornecedores();
  }

  fetchFornecedores(): void {
    this.loading = true;
    this.error = null;
    this.fornecedorService.listarTodos().subscribe({
      next: (data) => {
        this.fornecedores = data ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Falha ao carregar fornecedores.';
        console.error('Erro ao buscar fornecedores', err);
        this.loading = false;
      },
    });
  }

  showCreateForm(): void {
    this.editingFornecedor = null;
    this.resetForm();
    this.showForm = true;
  }

  editFornecedor(fornecedor: Fornecedor): void {
    this.editingFornecedor = fornecedor;
    this.formData = {
      nome: fornecedor.nome,
      tipo: fornecedor.tipo,
      cpf: fornecedor.cpf || '',
      cnpj: fornecedor.cnpj || '',
      pais: fornecedor.pais
    };
    this.showForm = true;
  }

  deleteFornecedor(fornecedor: Fornecedor): void {
    if (confirm(`Tem certeza que deseja deletar o fornecedor "${fornecedor.nome}"?`)) {
      this.fornecedorService.deletar(fornecedor.id!).subscribe({
        next: () => {
          this.fetchFornecedores(); // Recarrega a lista
        },
        error: (err) => {
          this.error = 'Erro ao deletar fornecedor.';
          console.error('Erro ao deletar fornecedor', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.isFormValid()) {
      this.error = 'Por favor, preencha todos os campos obrigatórios.';
      return;
    }

    const fornecedorData = {
      nome: this.formData.nome,
      tipo: this.formData.tipo,
      cpf: this.formData.tipo === 'F' ? this.formData.cpf : undefined,
      cnpj: this.formData.tipo === 'J' ? this.formData.cnpj : undefined,
      pais: this.formData.pais
    };

    if (this.editingFornecedor) {
      // Update existing fornecedor
      this.fornecedorService.atualizar(this.editingFornecedor.id!, fornecedorData).subscribe({
        next: () => {
          this.showForm = false;
          this.fetchFornecedores();
        },
        error: (err) => {
          this.error = 'Erro ao atualizar fornecedor.';
          console.error('Erro ao atualizar fornecedor', err);
        }
      });
    } else {
      // Create new fornecedor
      this.fornecedorService.criar(fornecedorData).subscribe({
        next: () => {
          this.showForm = false;
          this.fetchFornecedores();
        },
        error: (err) => {
          this.error = 'Erro ao criar fornecedor.';
          console.error('Erro ao criar fornecedor', err);
        }
      });
    }
  }

  cancelForm(): void {
    this.showForm = false;
    this.resetForm();
    this.error = null;
  }

  resetForm(): void {
    this.formData = {
      nome: '',
      tipo: 'F',
      cpf: '',
      cnpj: '',
      pais: ''
    };
  }

  isFormValid(): boolean {
    const hasNome = this.formData.nome.trim().length > 0;
    const hasPais = this.formData.pais.trim().length > 0;
    const hasValidDocument = this.formData.tipo === 'F'
      ? this.formData.cpf.trim().length > 0
      : this.formData.cnpj.trim().length > 0;

    return hasNome && hasPais && hasValidDocument;
  }

  onTipoChange(): void {
    // Clear document fields when switching type
    this.formData.cpf = '';
    this.formData.cnpj = '';
  }

  trackByFornecedor(index: number, fornecedor: Fornecedor): number {
    return fornecedor.id || index;
  }
}
