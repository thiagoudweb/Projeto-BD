import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Armazem } from '../../models/armazem';
import { ArmazemService } from '../../services/armazem.service';

@Component({
  selector: 'app-armazem-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './armazem-list.component.html',
  styleUrl: './armazem-list.component.css'
})
export class ArmazemListComponent implements OnInit {
  armazens: Armazem[] = [];
  loading = false;
  error: string | null = null;
  showForm = false;
  editingArmazem: Armazem | null = null;

  // Form fields
  formData = {
    nome: ''
  };

  constructor(private armazemService: ArmazemService) {}

  ngOnInit(): void {
    this.fetchArmazens();
  }

  fetchArmazens(): void {
    this.loading = true;
    this.error = null;
    this.armazemService.listarTodos().subscribe({
      next: (data) => {
        this.armazens = data ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Falha ao carregar armazéns.';
        console.error('Erro ao buscar armazéns', err);
        this.loading = false;
      },
    });
  }

  showCreateForm(): void {
    this.editingArmazem = null;
    this.resetForm();
    this.showForm = true;
  }

  editArmazem(armazem: Armazem): void {
    this.editingArmazem = armazem;
    this.formData = {
      nome: armazem.nome
    };
    this.showForm = true;
  }

  deleteArmazem(armazem: Armazem): void {
    if (confirm(`Tem certeza que deseja deletar o armazém "${armazem.nome}"?`)) {
      this.armazemService.deletar(armazem.id).subscribe({
        next: () => {
          this.fetchArmazens(); // Recarrega a lista
        },
        error: (err) => {
          this.error = 'Erro ao deletar armazém.';
          console.error('Erro ao deletar armazém', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.formData.nome.trim()) {
      this.error = 'Nome é obrigatório.';
      return;
    }

    const armazemData: Omit<Armazem, 'id'> = {
      nome: this.formData.nome.trim()
    };

    if (this.editingArmazem) {
      // Editando armazém existente
      this.armazemService.atualizar(this.editingArmazem.id, armazemData).subscribe({
        next: () => {
          this.fetchArmazens();
          this.cancelForm();
        },
        error: (err) => {
          this.error = 'Erro ao atualizar armazém.';
          console.error('Erro ao atualizar armazém', err);
        }
      });
    } else {
      // Criando novo armazém
      this.armazemService.criar(armazemData).subscribe({
        next: () => {
          this.fetchArmazens();
          this.cancelForm();
        },
        error: (err) => {
          this.error = 'Erro ao criar armazém.';
          console.error('Erro ao criar armazém', err);
        }
      });
    }
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingArmazem = null;
    this.resetForm();
    this.error = null;
  }

  resetForm(): void {
    this.formData = {
      nome: ''
    };
  }

  trackByIndex(index: number): number {
    return index;
  }
}
