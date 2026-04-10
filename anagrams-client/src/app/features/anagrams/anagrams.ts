import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AnagramsService, AnagramsResponse } from '../../core/services/anagrams.service';

@Component({
    selector: 'app-anagrams',
    standalone: true,
    imports: [FormsModule],
    styles: [`
    .page-header { margin-bottom: var(--space-6); }
    .page-header h1 { font-size: 2rem; }
    .page-header p  { color: var(--colour-text-muted); margin-top: var(--space-2); }

    .search-card { margin-bottom: var(--space-5); }

    .input-row {
      display: flex;
      gap: var(--space-3);
    }

    .input-row .input { flex: 1; }

    .results-card { }

    .results-header {
      margin-bottom: var(--space-5);
    }

    .results-header h2 {
      font-family: var(--font-display);
      font-size: 1.25rem;
      font-weight: 400;
    }

    .results-header p {
      color: var(--colour-text-muted);
      font-size: 0.875rem;
      margin-top: var(--space-1);
    }

    .anagram-list {
      list-style: none;
      border-top: 1px solid var(--colour-border);
    }

    .anagram-item {
      padding: var(--space-3) 0;
      border-bottom: 1px solid var(--colour-border);
      font-size: 0.95rem;
      animation: fadeUp 200ms ease forwards;
    }

    .empty {
      padding: var(--space-8) 0;
      text-align: center;
      color: var(--colour-text-muted);
    }

    .tag {
      display: inline-block;
      padding: var(--space-1) var(--space-3);
      background: var(--colour-accent-light);
      color: var(--colour-accent);
      border-radius: var(--radius-sm);
      font-size: 0.8rem;
      font-weight: 500;
      margin-left: var(--space-2);
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
        <h1>Anagrams</h1>
        <p>Find all anagrams of a word within the dictionary.</p>
      </div>

      <!-- Search -->
      <div class="card search-card">
        <div class="input-row">
          <input
            class="input"
            type="text"
            placeholder="Enter a word…"
            [(ngModel)]="searchWord"
            (keyup.enter)="search()"
          />
          <button
            class="btn btn--primary"
            (click)="search()"
            [disabled]="!searchWord().trim() || loading()"
          >
            {{ loading() ? 'Searching…' : 'Find anagrams' }}
          </button>
        </div>
        @if (error()) {
          <p class="error">{{ error() }}</p>
        }
      </div>

      <!-- Results -->
      @if (result()) {
        <div class="card results-card page-enter">
          <div class="results-header">
            <h2>
              Results for "{{ result()!.word }}"
              <span class="tag">{{ result()!.anagrams.length }} found</span>
            </h2>
            @if (result()!.anagrams.length > 0) {
              <p>All words in the dictionary that are anagrams of "{{ result()!.word }}".</p>
            }
          </div>

          @if (result()!.anagrams.length === 0) {
            <p class="empty">No anagrams found for "{{ result()!.word }}".</p>
          } @else {
            <ul class="anagram-list">
              @for (anagram of result()!.anagrams; track anagram) {
                <li class="anagram-item">{{ anagram }}</li>
              }
            </ul>
          }
        </div>
      }

    </div>
  `
})
export class AnagramsComponent {

    private readonly svc = inject(AnagramsService);

    // ── State ────────────────────────────────────────────────────────────────

    searchWord = signal('');
    result     = signal<AnagramsResponse | null>(null);
    loading    = signal(false);
    error      = signal('');

    // ── Actions ──────────────────────────────────────────────────────────────

    search(): void {
        const word = this.searchWord().trim();
        if (!word) return;

        this.loading.set(true);
        this.error.set('');
        this.result.set(null);

        this.svc.getAnagrams(word).subscribe({
            next: res => {
                this.result.set(res);
                this.loading.set(false);
            },
            error: () => {
                this.error.set('Failed to fetch anagrams. Is the service running?');
                this.loading.set(false);
            }
        });
    }
}
