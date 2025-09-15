import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Categoria } from '../../models/categoria';
import { CategoriaService } from '../../services/categoria.service';

@Component({
  selector: 'app-categoria-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categoria-list.component.html',
  styleUrl: './categoria-list.component.css'
})
export class CategoriaListComponent implements OnInit {
  categorias: Categoria[] = [];
  loading = false;
  error: string | null = null;

  // Form state
  showForm = false;
  editingCategoria: Categoria | null = null;
  formData: Categoria = {
    id: undefined,
    nome: '',
    descricao: ''
  };

  constructor(private categoriaService: CategoriaService) {}

  ngOnInit(): void {
    this.carregarCategorias();
  }

  carregarCategorias(): void {
    this.loading = true;
    this.error = null;

    this.categoriaService.listarTodos().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar categorias';
        this.loading = false;
        console.error('Erro:', error);
      }
    });
  }

  showCreateForm(): void {
    this.editingCategoria = null;
    this.formData = {
      id: undefined,
      nome: '',
      descricao: ''
    };
    this.showForm = true;
  }

  editCategoria(categoria: Categoria): void {
    this.editingCategoria = categoria;
    this.formData = { ...categoria };
    this.showForm = true;
  }

  onSubmit(): void {
    if (!this.formData.nome.trim()) {
      this.error = 'Por favor, preencha o nome da categoria';
      return;
    }

    this.error = null;

    if (this.editingCategoria) {
      // Atualizar categoria existente
      this.categoriaService.atualizar(this.editingCategoria.id!, this.formData).subscribe({
        next: () => {
          this.carregarCategorias();
          this.showForm = false;
          this.editingCategoria = null;
        },
        error: (error) => {
          this.error = 'Erro ao atualizar categoria';
          console.error('Erro:', error);
        }
      });
    } else {
      // Criar nova categoria
      this.categoriaService.criar(this.formData).subscribe({
        next: () => {
          this.carregarCategorias();
          this.showForm = false;
        },
        error: (error) => {
          this.error = 'Erro ao criar categoria';
          console.error('Erro:', error);
        }
      });
    }
  }

  deleteCategoria(categoria: Categoria): void {
    if (confirm(`Tem certeza que deseja excluir a categoria ${categoria.nome}?`)) {
      this.categoriaService.deletar(categoria.id!).subscribe({
        next: () => {
          this.carregarCategorias();
        },
        error: (error) => {
          this.error = 'Erro ao excluir categoria';
          console.error('Erro:', error);
        }
      });
    }
  }
}
