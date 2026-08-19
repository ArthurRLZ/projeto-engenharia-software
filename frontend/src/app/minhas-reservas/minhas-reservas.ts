import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReservationService, MinhaReservaResponse, Page } from '../services/reservation.service';

@Component({
  selector: 'app-minhas-reservas',
  imports: [CommonModule, RouterLink],
  templateUrl: './minhas-reservas.html',
  styleUrl: './minhas-reservas.css',
})
export class MinhasReservas implements OnInit {
  private reservationService = inject(ReservationService);

  reservas: MinhaReservaResponse[] = [];
  isLoading = true;
  errorMessage: string | null = null;

  // paginacao
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;

  ngOnInit(): void {
    this.carregarReservas();
  }

  carregarReservas(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.reservationService.listarMinhasReservas(this.currentPage, this.pageSize).subscribe({
      next: (page: Page<MinhaReservaResponse>) => {
        this.reservas = page.content;
        this.totalPages = page.totalPages;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Erro ao carregar reservas. Tente novamente.';
      }
    });
  }

  irParaPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPages) {
      this.currentPage = pagina;
      this.carregarReservas();
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'CONFIRMADA': return 'badge-confirmada';
      case 'PENDENTE': return 'badge-pendente';
      case 'RECUSADA': return 'badge-recusada';
      case 'CANCELADA': return 'badge-cancelada';
      default: return '';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'CONFIRMADA': return '✅ Confirmada';
      case 'PENDENTE': return '⏳ Pendente';
      case 'RECUSADA': return '❌ Recusada';
      case 'CANCELADA': return '🚫 Cancelada';
      default: return status;
    }
  }
}