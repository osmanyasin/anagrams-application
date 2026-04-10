import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, RouterLink, RouterLinkActive],
    styles: [`
    nav {
      border-bottom: 1px solid var(--colour-border);
      background: var(--colour-surface);
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .nav-inner {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 60px;
    }

    .nav-brand {
      font-family: var(--font-display);
      font-size: 1.25rem;
      color: var(--colour-text);
      text-decoration: none;
      letter-spacing: -0.01em;
    }

    .nav-links {
      display: flex;
      gap: var(--space-1);
      list-style: none;
    }

    .nav-links a {
      display: block;
      padding: var(--space-2) var(--space-4);
      font-size: 0.9rem;
      font-weight: 500;
      color: var(--colour-text-muted);
      text-decoration: none;
      border-radius: var(--radius-md);
      transition: color var(--transition), background var(--transition);

      &:hover {
        color: var(--colour-text);
        background: var(--colour-bg);
      }

      &.active {
        color: var(--colour-accent);
        background: var(--colour-accent-light);
      }
    }

    main {
      padding: var(--space-8) 0;
    }
  `],
    template: `
    <nav>
      <div class="container nav-inner">
        <a class="nav-brand" routerLink="/words">Anagrams</a>
        <ul class="nav-links">
          <li><a routerLink="/words"   routerLinkActive="active">Words</a></li>
          <li><a routerLink="/anagrams" routerLinkActive="active">Anagrams</a></li>
          <li><a routerLink="/stats"   routerLinkActive="active">Stats</a></li>
        </ul>
      </div>
    </nav>

    <main>
      <div class="container">
        <router-outlet />
      </div>
    </main>
  `
})
export class AppComponent {}
