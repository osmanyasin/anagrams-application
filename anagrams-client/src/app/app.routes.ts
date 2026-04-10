import {Routes} from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'words',
        pathMatch: 'full'
    },
    {
        path: 'words',
        loadComponent: () =>
            import('./features/words/words').then(m => m.WordsComponent)
    },
    {
        path: 'anagrams',
        loadComponent: () =>
            import('./features/anagrams/anagrams').then(m => m.AnagramsComponent)
    },
    {
        path: 'stats',
        loadComponent: () =>
            import('./features/stats/stats').then(m => m.StatsComponent)
    },
    {
        path: '**',
        redirectTo: 'words'
    }
];
