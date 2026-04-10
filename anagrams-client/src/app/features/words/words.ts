import {Component, inject, OnInit, signal} from '@angular/core';
import {DecimalPipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {AnagramsService, Page} from '../../core/services/anagrams.service';

@Component({
    selector: 'app-words',
    standalone: true,
    imports: [FormsModule, DecimalPipe],
    styles: [`
    .page-header { margin-bottom: var(--space-6); }
    .page-header h1 { font-size: 2rem; }
    .page-header p  { color: var(--colour-text-muted); margin-top: var(--space-2); }

    .add-card { margin-bottom: var(--space-5); }

    .input-row {
      display: flex;
      gap: var(--space-3);
    }

    .input-row .input { flex: 1; }

    .words-card { }

    .words-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: var(--space-5);
    }

    .words-header h2 {
      font-family: var(--font-display);
      font-size: 1.25rem;
      font-weight: 400;
    }

    .total {
      font-size: 0.85rem;
      color: var(--colour-text-muted);
    }

    .word-list {
      list-style: none;
      border-top: 1px solid var(--colour-border);
    }

    .word-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: var(--space-3) 0;
      border-bottom: 1px solid var(--colour-border);
      animation: fadeUp 200ms ease forwards;

      &:hover .btn--danger { opacity: 1; }
    }

    .word-text {
      font-size: 0.95rem;
    }

    .word-item .btn--danger {
      opacity: 0;
      transition: opacity var(--transition);
    }

    .empty {
      padding: var(--space-8) 0;
      text-align: center;
      color: var(--colour-text-muted);
    }

    .pagination {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-3);
      margin-top: var(--space-5);
    }

    .page-info {
      font-size: 0.875rem;
      color: var(--colour-text-muted);
    }

    .feedback {
      font-size: 0.85rem;
      margin-top: var(--space-3);
      min-height: 1.2em;

      &.success { color: var(--colour-accent); }
      &.error   { color: var(--colour-danger); }
    }
  `],
    template: `
    <div class="page-enter">

      <div class="page-header">
        <h1>Dictionary</h1>
        <p>Add, browse and remove words from the dictionary.</p>
      </div>

      <!-- Add word -->
      <div class="card add-card">
        <div class="input-row">
          <input
            class="input"
            type="text"
            placeholder="Enter a word…"
            [(ngModel)]="newWord"
            (keyup.enter)="addWord()"
          />
          <button
            class="btn btn--primary"
            (click)="addWord()"
            [disabled]="!newWord().trim() || adding()"
          >
            {{ adding() ? 'Adding…' : 'Add word' }}
          </button>
        </div>
        @if (feedback()) {
          <p class="feedback" [class.success]="feedbackType() === 'success'" [class.error]="feedbackType() === 'error'">
            {{ feedback() }}
          </p>
        }
      </div>

      <!-- Word list -->
      <div class="card words-card">
        <div class="words-header">
          <h2>All words</h2>
          @if (page()) {
            <span class="total">{{ page()!.page.totalElements | number }} words</span>
          }
        </div>

        @if (loading()) {
          <p class="empty text-muted">Loading…</p>
        } @else if (words().length === 0) {
          <p class="empty">No words in the dictionary yet.</p>
        } @else {
          <ul class="word-list">
            @for (word of words(); track word) {
              <li class="word-item">
                <span class="word-text">{{ word }}</span>
                <button
                  class="btn btn--danger btn--sm"
                  (click)="deleteWord(word)"
                >
                  Delete
                </button>
              </li>
            }
          </ul>

          <!-- Pagination -->
          @if (page() && page()!.page.totalPages > 1) {
            <div class="pagination">
              <button
                class="btn btn--ghost btn--sm"
                (click)="changePage(currentPage() - 1)"
                [disabled]="currentPage() === 0"
              >
                ← Prev
              </button>
              <span class="page-info">
                Page {{ currentPage() + 1 }} of {{ page()!.page.totalPages }}
              </span>
              <button
                class="btn btn--ghost btn--sm"
                (click)="changePage(currentPage() + 1)"
                [disabled]="currentPage() + 1 >= page()!.page.totalPages"
              >
                Next →
              </button>
            </div>
          }
        }
      </div>

    </div>
  `
})
export class WordsComponent implements OnInit {

    private readonly svc = inject(AnagramsService);

    // ── State ────────────────────────────────────────────────────────────────

    page = signal<Page<string> | null>(null);
    words = signal<string[]>([]);
    currentPage = signal(0);
    loading = signal(false);
    adding = signal(false);
    newWord = signal('');
    feedback = signal('');
    feedbackType = signal<'success' | 'error'>('success');

    // ── Lifecycle ────────────────────────────────────────────────────────────

    ngOnInit(): void {
        this.loadWords();
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    loadWords(): void {
        this.loading.set(true);
        this.svc.getWords(this.currentPage()).subscribe({
            next: p => {
                this.page.set(p);
                this.words.set(p.content);
                this.loading.set(false);
            },
            error: () => {
                this.showFeedback('Failed to load words.', 'error');
                this.loading.set(false);
            }
        });
    }

    addWord(): void {
        const word = this.newWord().trim();
        if (!word) return;

        this.adding.set(true);
        this.svc.addWords([word]).subscribe({
            next: () => {
                this.newWord.set('');
                this.showFeedback(`"${word}" added successfully.`, 'success');
                this.adding.set(false);
                this.loadWords();
            },
            error: () => {
                this.showFeedback('Failed to add word.', 'error');
                this.adding.set(false);
            }
        });
    }

    deleteWord(word: string): void {
        this.svc.deleteWords([word]).subscribe({
            next: () => {
                this.showFeedback(`"${word}" deleted.`, 'success');
                this.loadWords();
            },
            error: () => this.showFeedback('Failed to delete word.', 'error')
        });
    }

    changePage(page: number): void {
        this.currentPage.set(page);
        this.loadWords();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private showFeedback(message: string, type: 'success' | 'error'): void {
        this.feedback.set(message);
        this.feedbackType.set(type);
        setTimeout(() => this.feedback.set(''), 3000);
    }
}
