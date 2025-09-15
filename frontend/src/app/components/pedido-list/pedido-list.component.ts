import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Pedido, PedidoProduto } from '../../models/pedido';
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
    status: '',
    produtos: [] as PedidoProduto[]
  };

  // Campo para adicionar novo produto
  novoProduto: PedidoProduto = {
    idProduto: 0,
    quantidade: 1
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
      status: '',
      produtos: []
    };
    this.novoProduto = {
      idProduto: 0,
      quantidade: 1
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
      status: pedido.status || '',
      produtos: []
    };

    // Buscar produtos do pedido se está editando
    if (pedido.idPedido) {
      this.carregarProdutosDoPedido(pedido.idPedido);
    }

    this.showForm = true;
  }

  carregarProdutosDoPedido(pedidoId: number): void {
    this.pedidoService.buscarProdutosDoPedido(pedidoId).subscribe({
      next: (produtos) => {
        this.formData.produtos = produtos.map(p => ({
          idProduto: p.idProdutoValue || p.idProduto,
          quantidade: p.quantidade
        }));
      },
      error: (err) => {
        console.error('Erro ao carregar produtos do pedido', err);
      }
    });
  }

  adicionarProduto(): void {
    if (this.novoProduto.idProduto > 0 && this.novoProduto.quantidade > 0) {
      // Verificar se o produto já existe na lista
      const produtoExistente = this.formData.produtos.find(p => p.idProduto === this.novoProduto.idProduto);

      if (produtoExistente) {
        produtoExistente.quantidade += this.novoProduto.quantidade;
      } else {
        this.formData.produtos.push({ ...this.novoProduto });
      }

      // Resetar o campo de novo produto
      this.novoProduto = {
        idProduto: 0,
        quantidade: 1
      };
    }
  }

  removerProduto(index: number): void {
    this.formData.produtos.splice(index, 1);
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
      dataPedido: new Date().toISOString().slice(0, 19),
      prazoEntrega: this.formData.prazoEntrega || undefined,
      precoFinal: this.formData.precoFinal,
      modoEncomenda: this.formData.modoEncomenda || '',
      status: this.formData.status || ''
    };

    if (this.editingPedido) {
      // Atualizar pedido existente
      this.pedidoService.atualizar(this.editingPedido.idPedido, pedidoPayload).subscribe({
        next: (pedidoAtualizado) => {
          this.atualizarProdutosDoPedido(pedidoAtualizado.idPedido);
        },
        error: (err) => {
          this.error = 'Falha ao atualizar pedido: ' + (err.error?.message || err.message || 'Erro desconhecido');
          console.error('Erro ao atualizar pedido', err);
        }
      });
    } else {
      // Criar novo pedido
      this.pedidoService.criar(pedidoPayload).subscribe({
        next: (novoPedido) => {
          this.adicionarProdutosAoPedido(novoPedido.idPedido);
        },
        error: (err) => {
          this.error = 'Falha ao criar pedido: ' + (err.error?.message || err.message || 'Erro desconhecido');
          console.error('Erro ao criar pedido', err);
        }
      });
    }
  }

  private adicionarProdutosAoPedido(pedidoId: number): void {
    if (this.formData.produtos.length === 0) {
      this.finalizarSubmissao();
      return;
    }

    let produtosProcessados = 0;
    const totalProdutos = this.formData.produtos.length;

    this.formData.produtos.forEach(produto => {
      this.pedidoService.adicionarProdutoAoPedido(pedidoId, produto.idProduto, produto.quantidade).subscribe({
        next: () => {
          produtosProcessados++;
          if (produtosProcessados === totalProdutos) {
            this.finalizarSubmissao();
          }
        },
        error: (err) => {
          console.error('Erro ao adicionar produto ao pedido', err);
          produtosProcessados++;
          if (produtosProcessados === totalProdutos) {
            this.finalizarSubmissao();
          }
        }
      });
    });
  }

  private atualizarProdutosDoPedido(pedidoId: number): void {
    // Para atualização, primeiro removemos todos os produtos existentes
    // e depois adicionamos os novos (implementação simplificada)
    this.adicionarProdutosAoPedido(pedidoId);
  }

  private finalizarSubmissao(): void {
    this.fetchPedidos();
    this.showForm = false;
  }
}
