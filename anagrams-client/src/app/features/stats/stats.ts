import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { AnagramsService, AnagramCountResponse } from '../../core/services/anagrams.service';

@Component({
    selector: 'app-stats',
    standalone: true,
    imports: [DecimalPipe],
    styles: [`
    .page-header { margin-bottom: var(--space-6); }
    .page-header h1 { font-size: 2rem; }
    .page-header p  { color: var(--colour-text-muted); margin-top: var(--space-2); }

    .actions {
      margin-bottom: var(--space-5);
      display: flex;
      align-items: center;
      gap: var(--space-4);
    }

    .elapsed {
      font-size: 0.85rem;
      color: var(--colour-text-muted);
    }

    .elapsed span {
      color: var(--colour-accent);
      font-weight: 500;
    }

    .stats-card { }

    .stats-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: var(--space-5);
    }

    .stats-header h2 {
      font-family: var(--font-display);
      font-size: 1.25rem;
      font-weight: 400;
    }

    .total-badge {
      font-size: 0.85rem;
      color: var(--colour-text-muted);
    }

    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 0.9rem;
    }

    thead th {
      text-align: left;
      font-weight: 500;
      font-size: 0.8rem;
      text-transform: uppercase;
      letter-spacing: 0.06em;
      color: var(--colour-text-muted);
      padding: var(--space-2) var(--space-3);
      border-bottom: 2px solid var(--colour-border);
    }

    tbody tr {
      border-bottom: 1px solid var(--colour-border);
      transition: background var(--transition);
      animation: fadeUp 200ms ease forwards;

      &:hover { background: var(--colour-bg); }
      &:last-child { border-bottom: none; }
    }

    tbody td {
      padding: var(--space-3);
      color: var(--colour-text);
    }

    .length-sentence {
      font-size: 0.9rem;
      margin-bottom: var(--space-2);
    
      strong {
        font-weight: 600;
        color: var(--colour-accent);
      }
    }

    .bar-track {
      width: 100%;
      height: 6px;
      background: var(--colour-border);
      border-radius: 99px;
      overflow: hidden;
    }

    .bar-fill {
      height: 100%;
      background: var(--colour-accent);
      border-radius: 99px;
      transition: width 400ms ease;
    }

    .empty {
      padding: var(--space-8) 0;
      text-align: center;
      color: var(--colour-text-muted);
    }

    .error {
      color: var(--colour-danger);
      font-size: 0.875rem;
      margin-top: var(--space-3);
    }
  `],
    template: `
    <div class="page-enter">

      <div class="page-header">
        <h1>Stats</h1>
        <p>Anagram group counts for every word length in the dictionary.</p>
      </div>

      <div class="actions">
        <button
          class="btn btn--primary"
          (click)="load()"
          [disabled]="loading()"
        >
          {{ loading() ? 'Calculating…' : result() ? 'Recalculate' : 'Calculate' }}
        </button>

        @if (result()) {
          <p class="elapsed">
            Calculated in <span>{{ result()!.elapsedMs | number }}ms</span>
          </p>
        }
      </div>

      @if (error()) {
        <p class="error">{{ error() }}</p>
      }

      @if (result()) {
        <div class="card stats-card page-enter">
          <div class="stats-header">
            <h2>Anagram groups by word length</h2>
            <span class="total-badge">{{ rows().length }} word lengths</span>
          </div>

          @if (rows().length === 0) {
            <p class="empty">No anagram groups found in the dictionary.</p>
          } @else {
            <table>
              <thead>
                <tr>
                  <th>Summary</th>
                </tr>
              </thead>
              <tbody>
                @for (row of rows(); track row.length) {
                  <tr>
                    <td>
                      <p class="length-sentence">
                        Words with the character length of <strong>{{ row.length }}</strong> had <strong>{{ row.count | number }}</strong> anagram groups
                      </p>
                      <div class="bar-track">
                        <div
                          class="bar-fill"
                          [style.width.%]="percentage(row.count)"
                        ></div>
                      </div>
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          }
        </div>
      }

    </div>
  `
})
export class StatsComponent {

    private readonly svc = inject(AnagramsService);

    // ── State ────────────────────────────────────────────────────────────────

    result  = signal<AnagramCountResponse | null>(null);
    rows    = signal<{ length: number; count: number }[]>([]);
    loading = signal(false);
    error   = signal('');

    // ── Actions ──────────────────────────────────────────────────────────────

    load(): void {
        this.loading.set(true);
        this.error.set('');

        this.svc.getAnagramCounts().subscribe({
            next: res => {
                this.result.set(res);
                this.rows.set(
                    Object.entries(res.countsByLength).map(([length, count]) => ({
                        length: Number(length),
                        count
                    }))
                );
                this.loading.set(false);
            },
            error: () => {
                this.error.set('Failed to fetch stats. Is the service running?');
                this.loading.set(false);
            }
        });
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    percentage(count: number): number {
        const max = Math.max(...this.rows().map(r => r.count));
        return max === 0 ? 0 : (count / max) * 100;
    }
}
