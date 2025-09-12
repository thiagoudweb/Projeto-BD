import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'clientes' },
  {
    path: 'clientes',
    loadComponent: () =>
      import('./components/cliente-list/cliente-list.component').then(
        (m) => m.ClienteListComponent
      ),
  },
  {
    path: 'pedidos',
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
  // Placeholder routes for navbar navigation - you can replace these with actual components
  { path: 'estoque', redirectTo: 'clientes' }, // Replace with actual estoque component when created
  { path: 'enderecos', redirectTo: 'clientes' }, // Replace with actual endereco component when created
  { path: 'entregadoras', redirectTo: 'clientes' }, // Replace with actual entregadora component when created
  { path: '**', redirectTo: 'clientes' },
];
