import {inject, Injectable, Signal, signal} from "@angular/core";
import {Account, Authority} from "./account.module";
import {catchError, Observable, of, ReplaySubject, shareReplay, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {StateStorageService} from "./state-storage.service";
import {environment} from "../../../enviroments/enviroment.development";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
    private userIdentity = signal<Account | null>(null);
    private authenticationState = new ReplaySubject<Account | null>(1);
    private accountCache$?: Observable<Account> | null;

    private http = inject(HttpClient);
    private router = inject(Router);

    private stateStorageService = inject(StateStorageService);

    authenticate(identity: Account | null): void {
        this.userIdentity.set(identity);
        this.authenticationState.next(this.userIdentity());
        if (!identity) {
            this.accountCache$ = null;
        }
    }

    trackCurrentAccount(): Signal<Account | null> {
        return this.userIdentity.asReadonly();
    }

    hasAnyAuthority(authorities: Authority[] | Authority): boolean {
        const userIdentity = this.userIdentity();
        if (!userIdentity) {
            return false;
        }
        if (!Array.isArray(authorities)) {
            authorities = [authorities];
        }
        return userIdentity.authorities.some((authority: Authority) => authorities.includes(authority));
    }

    identity(force?: boolean): Observable<Account | null> {
        if (!this.accountCache$ || force) {
            this.accountCache$ = this.fetch().pipe(
                tap((account: Account) => {
                    this.userIdentity.set(account);
                    this.navigateToStoredUrl();
                }),
                shareReplay()
            );
        }
        return this.accountCache$.pipe(catchError(() => of(null)));
    }

    isAuthenticated(): boolean {
        return this.userIdentity() !== null;
    }

    private fetch(): Observable<Account> {
        return this.http.get<Account>(environment.account);
    }

    private navigateToStoredUrl(): void {
        const previousUrl = this.stateStorageService.getUrl();
        if (previousUrl) {
            this.stateStorageService.clearUrl();
            this.router.navigateByUrl(previousUrl);
        }
    }
}
