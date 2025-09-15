import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router) {}

  currentRoute: string = '';

  ngOnInit() {
    this.currentRoute = this.router.url;

    // Listen to route changes to update the active indicator
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.currentRoute = event.url;
    });
  }

  navigateToHome() {
    this.router.navigate(['/']);
  }

  navigateToClientes() {
    this.router.navigate(['/clientes']);
  }

  navigateToPedidos() {
    this.router.navigate(['/pedidos-produtos']);
  }

  navigateToProdutos() {
    this.router.navigate(['/produtos']);
  }

  navigateToEstoque() {
    this.router.navigate(['/estoque']);
  }

  navigateToEnderecos() {
    this.router.navigate(['/enderecos']);
  }

  navigateToEntregadoras() {
    this.router.navigate(['/entregadoras']);
  }

  navigateToFornecedores() {
    this.router.navigate(['/fornecedores']);
  }

  navigateToFornecedorDetails() {
    this.router.navigate(['/fornecedor-details']);
  }

  isActive(route: string): boolean {
    return this.currentRoute === route;
  }

  // Substitua sua função antiga por esta
  getActiveIndicatorPosition(): number {
    // 1. Mapeia a rota para um índice (0 para o primeiro ícone, 1 para o segundo, etc.)
    const pathToIndexMap: { [key: string]: number } = {
      '/marketplace': 0,
      '/': 0,
      '/clientes': 1,
      '/pedidos-produtos': 2,
      '/produtos': 3,
      '/fornecedores': 4,
      '/fornecedor-details': 5,
      '/estoque': 6,
    };

    // Pega a rota base, como no seu código original
    const baseRoute = this.currentRoute.split('?')[0].split('#')[0];
    const activeIndex = pathToIndexMap[baseRoute as keyof typeof pathToIndexMap];

    // Se a rota não for encontrada no mapa, oculta o indicador
    if (activeIndex === undefined) {
      return -1;
    }

    // 2. Constantes que devem ser iguais às do seu CSS
    const paddingTop = 33;      // padding-top do .sidebar-contracted
    const iconHeight = 46;      // altura de cada .nav-icon
    const gap = 25;             // gap entre os ícones
    const indicatorHeight = 64;   // altura do .sidebar-contracted-item

    // 3. Cálculo para centralizar o indicador no ícone ativo
    const iconCenterPoint = (activeIndex * (iconHeight + gap)) + (iconHeight / 2);
    const topPosition = paddingTop + iconCenterPoint - (indicatorHeight / 2);

    return topPosition;
  }
}
