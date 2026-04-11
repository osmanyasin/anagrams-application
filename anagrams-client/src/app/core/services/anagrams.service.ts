import {HttpClient, HttpParams} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Page<T> {
    content: T[];
    page: {
        size: number;
        number: number;
        totalElements: number;
        totalPages: number;
    };
}

export interface AnagramsResponse {
    word: string;
    anagrams: string[];
}

export interface AnagramCountResponse {
    countsByLength: Record<number, number>;
    elapsedMs: number;
}

@Injectable({providedIn: 'root'})
export class AnagramsService {

    private readonly http = inject(HttpClient);
    private readonly base = environment.apiUrl;

    // ── Words ─────────────────────────────────────────────────────────────────

    getWords(page = 0, size = 50, sort = 'original,asc'): Observable<Page<string>> {
        const params = new HttpParams()
            .set('page', page)
            .set('size', size)
            .set('sort', sort);

        return this.http.get<Page<string>>(`${this.base}/words`, {params});
    }

    addWords(words: string[]): Observable<void> {
        return this.http.post<void>(`${this.base}/words`, words);
    }

    deleteWords(words: string[]): Observable<void> {
        return this.http.delete<void>(`${this.base}/words`, {body: words});
    }

    // ── Anagrams ──────────────────────────────────────────────────────────────

    getAnagrams(word: string): Observable<AnagramsResponse> {
        return this.http.get<AnagramsResponse>(`${this.base}/anagrams/${word}`);
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    getAnagramCounts(): Observable<AnagramCountResponse> {
        return this.http.get<AnagramCountResponse>(`${this.base}/anagrams/count`);
    }
}