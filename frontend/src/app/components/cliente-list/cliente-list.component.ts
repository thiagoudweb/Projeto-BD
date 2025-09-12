import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cliente } from '../../models/cliente';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.css'
})
export class ClienteListComponent implements OnInit {
  clientes: Cliente[] = [];
  loading = false;
  error: string | null = null;
  showForm = false;
  editingCliente: Cliente | null = null;

  // Form fields
  formData = {
    nome: '',
    limiteCredito: null as number | null,
    emails: [''],
    telefones: ['']
  };

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.fetchClientes();
  }

  fetchClientes(): void {
    this.loading = true;
    this.error = null;
    this.clienteService.listarTodos().subscribe({
      next: (data) => {
        this.clientes = data ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Falha ao carregar clientes.';
        console.error('Erro ao buscar clientes', err);
        this.loading = false;
      },
    });
  }

  showCreateForm(): void {
    this.editingCliente = null;
    this.resetForm();
    this.showForm = true;
  }

  editCliente(cliente: Cliente): void {
    this.editingCliente = cliente;
    this.formData = {
      nome: cliente.nome,
      limiteCredito: cliente.limiteCredito || null,
      emails: cliente.emails?.length ? [...cliente.emails] : [''],
      telefones: cliente.telefones?.length ? [...cliente.telefones] : ['']
    };
    this.showForm = true;
  }

  deleteCliente(cliente: Cliente): void {
    if (confirm(`Tem certeza que deseja deletar o cliente "${cliente.nome}"?`)) {
      this.clienteService.deletar(cliente.idCliente).subscribe({
        next: () => {
          this.fetchClientes(); // Recarrega a lista
        },
        error: (err) => {
          this.error = 'Erro ao deletar cliente.';
          console.error('Erro ao deletar cliente', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.formData.nome.trim()) {
      this.error = 'Nome é obrigatório.';
      return;
    }

    const clienteData: Omit<Cliente, 'idCliente'> = {
      nome: this.formData.nome.trim(),
      limiteCredito: this.formData.limiteCredito || undefined,
      emails: this.formData.emails.filter(email => email.trim()),
      telefones: this.formData.telefones.filter(tel => tel.trim())
    };

    if (this.editingCliente) {
      // Editando cliente existente
      this.clienteService.atualizar(this.editingCliente.idCliente, clienteData).subscribe({
        next: () => {
          this.fetchClientes();
          this.cancelForm();
        },
        error: (err) => {
          this.error = 'Erro ao atualizar cliente.';
          console.error('Erro ao atualizar cliente', err);
        }
      });
    } else {
      // Criando novo cliente
      this.clienteService.criar(clienteData).subscribe({
        next: () => {
          this.fetchClientes();
          this.cancelForm();
        },
        error: (err) => {
          this.error = 'Erro ao criar cliente.';
          console.error('Erro ao criar cliente', err);
        }
      });
    }
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingCliente = null;
    this.resetForm();
    this.error = null;
  }

  resetForm(): void {
    this.formData = {
      nome: '',
      limiteCredito: null,
      emails: [''],
      telefones: ['']
    };
  }

  addEmail(): void {
    this.formData.emails.push('');
  }

  removeEmail(index: number): void {
    if (this.formData.emails.length > 1) {
      this.formData.emails.splice(index, 1);
    }
  }

  addTelefone(): void {
    this.formData.telefones.push('');
  }

  removeTelefone(index: number): void {
    if (this.formData.telefones.length > 1) {
      this.formData.telefones.splice(index, 1);
    }
  }

  trackById(_index: number, item: Cliente): number {
    return item.idCliente;
  }

  trackByIndex(index: number): number {
    return index;
  }
}
