import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Pedido } from '../../models/pedido';
import { Cliente } from '../../models/cliente';
import { PedidoService } from '../../services/pedido.service';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-pedido-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedido-list.component.html',
  styleUrl: './pedido-list.component.css'
})
export class PedidoListComponent implements OnInit{
  pedidos: Pedido[] = [];
  clientes: Cliente[] = [];
  clientesMap: Map<number, Cliente> = new Map();
  loading = false;
  error: string | null = null;
  showForm = false;
  editingPedido: Pedido | null = null;

  // Form fields
  formData = {
    idCliente: null as number | null,
    dataPedido: '',
    prazoEntrega: '',
    precoFinal: null as number | null,
    modoEncomenda: '',
    status: ''
  };

  constructor(
    private pedidoService: PedidoService,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    this.fetchClientes();
    this.fetchPedidos();
  }

  fetchClientes(): void {
    this.clienteService.listarTodos().subscribe({
      next: (data) => {
        this.clientes = data ?? [];
        // Criar mapa para acesso rápido aos clientes por ID
        this.clientesMap.clear();
        this.clientes.forEach(cliente => {
          this.clientesMap.set(cliente.idCliente, cliente);
        });
      },
      error: (err) => {
        console.error('Erro ao buscar clientes', err);
      }
    });
  }

  getClienteNome(idCliente: number): number | string {
    const cliente = this.clientesMap.get(idCliente);
    return cliente ? cliente.idCliente : `Cliente ID: ${idCliente}`;
  }

  fetchPedidos(): void {
    this.loading = true;
    this.error = null;
    this.pedidoService.listarTodos().subscribe({
      next: (data) => {
        this.pedidos = data ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Falha ao carregar pedidos.';
        console.error('Erro ao buscar pedidos', err);
        this.loading = false;
      },
    });
  }

  showCreateForm(): void {
    this.editingPedido = null;
    this.resetForm();
    this.showForm = true;
  }

  resetForm(): void {
    this.formData = {
      idCliente: null,
      dataPedido: '',
      prazoEntrega: '',
      precoFinal: null,
      modoEncomenda: '',
      status: ''
    };
    this.error = null;
  }

  editPedido(pedido: Pedido): void {
    this.editingPedido = pedido;
    this.formData = {
      idCliente: pedido.idCliente,
      dataPedido: pedido.dataPedido || '',
      prazoEntrega: pedido.prazoEntrega || '',
      precoFinal: pedido.precoFinal,
      modoEncomenda: pedido.modoEncomenda || '',
      status: pedido.status || ''
    };
    this.showForm = true;
  }

  deletePedido(pedido: Pedido): void {
    if (confirm(`Tem certeza que deseja deletar o pedido #${pedido.idPedido}?`)) {
      this.pedidoService.deletar(pedido.idPedido).subscribe({
        next: () => {
          this.fetchPedidos(); // Recarrega a lista
        },
        error: (err) => {
          this.error = 'Falha ao deletar pedido.';
          console.error('Erro ao deletar pedido', err);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.formData.idCliente === null || this.formData.precoFinal === null) {
      this.error = 'ID do cliente e preço final são obrigatórios.';
      return;
    }

    // Validar se o cliente existe
    if (!this.clientesMap.has(this.formData.idCliente)) {
      this.error = `Cliente com ID ${this.formData.idCliente} não encontrado. Verifique se o ID está correto.`;
      return;
    }

    const pedidoPayload: Omit<Pedido, 'idPedido'> = {
      idCliente: this.formData.idCliente,
      // Criar data automaticamente no formato LocalDateTime (YYYY-MM-DDTHH:mm:ss)
      dataPedido: new Date().toISOString().slice(0, 19), // Remove os milissegundos e timezone
      prazoEntrega: this.formData.prazoEntrega || undefined,
      precoFinal: this.formData.precoFinal,
      modoEncomenda: this.formData.modoEncomenda || '',
      status: this.formData.status || ''
    };

    if (this.editingPedido) {
      // Atualizar pedido existente
      this.pedidoService.atualizar(this.editingPedido.idPedido, pedidoPayload).subscribe({
        next: () => {
          this.fetchPedidos();
          this.showForm = false;
        },
        error: (err) => {
          this.error = 'Falha ao atualizar pedido: ' + (err.error?.message || err.message || 'Erro desconhecido');
          console.error('Erro ao atualizar pedido', err);
        }
      });
    } else {
      // Criar novo pedido
      this.pedidoService.criar(pedidoPayload).subscribe({
        next: () => {
          this.fetchPedidos();
          this.showForm = false;
        },
        error: (err) => {
          this.error = 'Falha ao criar pedido: ' + (err.error?.message || err.message || 'Erro desconhecido');
          console.error('Erro ao criar pedido', err);
        }
      });
    }
  }
}
