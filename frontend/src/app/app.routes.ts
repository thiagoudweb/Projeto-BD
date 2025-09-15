import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'marketplace' },
  {
    path: 'marketplace',
    loadComponent: () =>
      import('./components/marketplace/marketplace.component').then(
        (m) => m.MarketplaceComponent
      ),
  },
  {
    path: 'clientes',
    loadComponent: () =>
      import('./components/cliente-list/cliente-list.component').then(
        (m) => m.ClienteListComponent
      ),
  },
  {
    path: 'pedidos-produtos',
    loadComponent: () =>
      import('./components/pedido-list/pedido-list.component').then(
        (m) => m.PedidoListComponent
      ),
  },
  {
    path: 'produtos',
    loadComponent: () =>
      import('./components/produto-list/produto-list.component').then(
        (m) => m.ProdutoListComponent
      ),
  },
  {
    path: 'estoque',
    loadComponent: () =>
      import('./components/estoque-list/estoque-list.component').then(
        (m) => m.EstoqueListComponent
      ),
  },
  {
    path: 'fornecedores',
    loadComponent: () =>
      import('./components/fornecedor-list/fornecedor-list.component').then(
        (m) => m.FornecedorListComponent
      ),
  },
  {
    path: 'fornecedor-details',
    loadComponent: () =>
      import('./components/fornecedor-details/fornecedor-details.component').then(
        (m) => m.FornecedorDetailsComponent
      ),
  },
  // Placeholder routes for navbar navigation - you can replace these with actual components
  { path: 'enderecos', redirectTo: 'clientes' }, // Replace with actual endereco component when created
  { path: 'entregadoras', redirectTo: 'clientes' }, // Replace with actual entregadora component when created
  { path: '**', redirectTo: 'clientes' },
];
